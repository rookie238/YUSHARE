package com.example.yushare.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.yushare.model.Comment
import com.example.yushare.model.Course
import com.example.yushare.model.NotificationItem
import com.example.yushare.model.Post
import com.example.yushare.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SharedViewModel : ViewModel() {

    // Firebase Araçları
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // --- LİSTELERİMİZ ---
    var coursesList = mutableStateListOf<Course>()
        private set

    // Kullanıcı profil bilgisini tutacak değişken
    var currentUserProfile = mutableStateOf(UserProfile())
        private set

    var postsList = mutableStateListOf<Post>()
        private set

    var selectedCoursePosts = mutableStateListOf<Post>()
        private set

    // --- YENİ: BİLDİRİM LİSTESİ ---
    var notificationsList = mutableStateListOf<NotificationItem>()
        private set

    // Yükleme yapılıyor mu? (Dönme efekti için)
    var isUploading = mutableStateOf(false)

    // --- YORUMLAR İÇİN LİSTE ---
    var commentsList = mutableStateListOf<Comment>()
        private set

    // Uygulama açılınca dersleri, gönderileri ve bildirimleri çek
    init {
        try {
            fetchCourses()
            fetchPosts()
            fetchUserProfile()
            listenToNotifications()
        } catch (e: Exception) { }
    }

    // --- DERSLERİ ÇEKME ---
    private fun fetchCourses() {
        db.collection("courses").get().addOnSuccessListener { result ->
            coursesList.clear()
            for (document in result) {
                coursesList.add(Course(
                    id = document.id,
                    title = document.getString("Title") ?: "",
                    subtitle = document.getString("Subtitle") ?: "",
                    term = document.getString("term") ?: "Fall 2025",
                    lecturer = document.getString("lecturer") ?: "Belirtilmemiş",
                    description = document.getString("description") ?: "Açıklama yok."
                ))
            }
        }
    }

    // --- PROFİL VERİSİNİ ÇEKME ---
    fun fetchUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("Users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        currentUserProfile.value = UserProfile(
                            name = document.getString("name") ?: "İsimsiz",
                            studentId = document.getString("id") ?: "",
                            department = document.getString("department") ?: "Bölüm Girilmemiş",
                            degree = document.getString("degree") ?: "Lisans",
                            bio = document.getString("bio") ?: ""
                        )
                    }
                }
        }
    }

    // --- PROFİLİ GÜNCELLEME ---
    fun updateUserProfile(newName: String, newBio: String, onResult: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        val updates = hashMapOf<String, Any>(
            "name" to newName,
            "bio" to newBio
        )

        db.collection("Users").document(userId)
            .update(updates)
            .addOnSuccessListener {
                currentUserProfile.value = currentUserProfile.value.copy(
                    name = newName,
                    bio = newBio
                )
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    // --- GÖNDERİLERİ ÇEKME ---
    fun fetchPosts() {
        db.collection("uploads")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                postsList.clear()
                for (document in result) {
                    postsList.add(mapDocumentToPost(document))
                }
            }
    }

    fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: "Mail adresi yok"
    }

    fun fetchPostsByCourse(courseCode: String) {
        selectedCoursePosts.clear()
        db.collection("uploads")
            .whereEqualTo("courseCode", courseCode)
            .get()
            .addOnSuccessListener { result ->
                val tempPosts = mutableListOf<Post>()
                for (document in result) {
                    tempPosts.add(mapDocumentToPost(document))
                }
                selectedCoursePosts.addAll(tempPosts.sortedByDescending { it.id })
            }
    }

    // Helper: Document -> Post Dönüşümü
    private fun mapDocumentToPost(document: com.google.firebase.firestore.DocumentSnapshot): Post {
        val url = document.getString("imageUrl") ?: document.getString("fileUrl") ?: ""
        return Post(
            id = document.id,
            ownerId = document.getString("ownerId") ?: "",
            username = document.getString("username") ?: "@anonim",
            courseCode = document.getString("courseCode") ?: "",
            fileUrl = url,
            description = document.getString("description") ?: "",
            fileType = document.getString("fileType") ?: "image"
        )
    }

    private fun getFileType(context: Context, uri: Uri): String {
        val mimeType = context.contentResolver.getType(uri) ?: return "file"
        return when {
            mimeType.startsWith("image") -> "image"
            mimeType.startsWith("audio") -> "audio"
            mimeType.contains("pdf") -> "pdf"
            mimeType.startsWith("video") -> "video"
            else -> "file"
        }
    }

    fun getCurrentUsername(): String {
        val user = auth.currentUser
        return when {
            user == null -> "@misafir"
            !user.displayName.isNullOrEmpty() -> user.displayName!!
            !user.email.isNullOrEmpty() -> "@${user.email!!.substringBefore("@")}"
            else -> "@kullanici"
        }
    }

    // --- YÜKLEME İŞLEMİ ---
    fun uploadPost(fileUri: Uri?, courseCode: String, description: String, context: Context, onSuccess: () -> Unit) {
        isUploading.value = true
        val currentUsername = getCurrentUsername()
        val currentUserId = auth.currentUser?.uid ?: ""

        fun createPostMap(downloadUrl: String, fileType: String): HashMap<String, Any> {
            return hashMapOf(
                "ownerId" to currentUserId,
                "username" to currentUsername,
                "courseCode" to courseCode,
                "fileUrl" to downloadUrl,
                "fileType" to fileType,
                "description" to description,
                "timestamp" to System.currentTimeMillis()
            )
        }

        if (fileUri == null) {
            val postMap = createPostMap("", "text")
            saveToFirestore(postMap, context, onSuccess)
            return
        }

        val fileType = getFileType(context, fileUri)

        MediaManager.get().upload(fileUri)
            .unsigned("ml_default")
            .option("resource_type", "auto")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    val downloadUrl = resultData?.get("secure_url").toString()
                    val postMap = createPostMap(downloadUrl, fileType)
                    saveToFirestore(postMap, context, onSuccess)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    isUploading.value = false
                    Toast.makeText(context, "Hata: ${error?.description}", Toast.LENGTH_LONG).show()
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            .dispatch()
    }

    private fun saveToFirestore(data: HashMap<String, Any>, context: Context, onSuccess: () -> Unit) {
        db.collection("uploads").add(data).addOnSuccessListener {
            isUploading.value = false
            Toast.makeText(context, "Paylaşıldı!", Toast.LENGTH_SHORT).show()
            fetchPosts()
            onSuccess()
        }.addOnFailureListener {
            isUploading.value = false
            Toast.makeText(context, "Hata: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // ==========================================
    // --- BİLDİRİM SİSTEMİ ---
    // ==========================================

    fun sendLikeNotification(post: Post) {
        val currentUser = auth.currentUser ?: return
        if (post.ownerId == currentUser.uid) return
        if (post.ownerId.isEmpty()) return

        val notificationData = hashMapOf(
            "toUserId" to post.ownerId,
            "fromUserName" to getCurrentUsername(),
            "actionType" to "LIKE",
            "message" to "liked your post",
            "timestamp" to System.currentTimeMillis(),
            "isRead" to false
        )
        db.collection("notifications").add(notificationData)
    }

    private fun listenToNotifications() {
        val myUserId = auth.currentUser?.uid ?: return
        db.collection("notifications")
            .whereEqualTo("toUserId", myUserId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener
                if (value != null) {
                    notificationsList.clear()
                    for (doc in value) {
                        notificationsList.add(
                            NotificationItem(
                                id = doc.id,
                                toUserId = doc.getString("toUserId") ?: "",
                                fromUserName = doc.getString("fromUserName") ?: "Unknown",
                                actionType = doc.getString("actionType") ?: "INFO",
                                message = doc.getString("message") ?: "",
                                timestamp = doc.getLong("timestamp") ?: 0L,
                                isRead = doc.getBoolean("isRead") ?: false
                            )
                        )
                    }
                }
            }
    }

    // ==========================================
    // --- YORUM SİSTEMİ (GÜNCELLENDİ) ---
    // ==========================================

    // 1. Yorumları "comments" koleksiyonundan çek (postId'ye göre filtrele)
    fun fetchComments(postId: String) {
        commentsList.clear()

        // "comments" ana koleksiyonunda postId'si eşleşenleri bul
        db.collection("comments")
            .whereEqualTo("postId", postId)
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener

                if (value != null) {
                    val tempComments = mutableListOf<Comment>()
                    for (document in value) {
                        // Veriyi Comment sınıfına dönüştür
                        val comment = document.toObject(Comment::class.java)
                        // ID'yi manuel set et (Firebase ID'si)
                        tempComments.add(comment.copy(id = document.id))
                    }
                    // Client tarafında sıralama yap (Firebase Index hatası almamak için)
                    commentsList.clear()
                    commentsList.addAll(tempComments.sortedBy { it.timestamp })
                }
            }
    }

    // 2. Yorum Gönder ("comments" ana koleksiyonuna ekle)
    fun sendComment(postId: String, commentText: String) {
        val currentUser = auth.currentUser ?: return
        val currentUsername = getCurrentUsername() // Yukarıdaki helper fonksiyonu kullan

        // Yeni Comment nesnesi oluştur
        val newComment = Comment(
            postId = postId,
            userId = currentUser.uid,
            username = currentUsername,
            text = commentText,
            timestamp = System.currentTimeMillis()
        )

        // Veritabanına ekle
        db.collection("comments").add(newComment)
            .addOnSuccessListener {
                println("Yorum başarıyla gönderildi.")
            }
            .addOnFailureListener { e ->
                println("Yorum hatası: ${e.localizedMessage}")
            }
    }

    // ID'ye göre gönderiyi bul
    fun getPostById(postId: String): Post? {
        return postsList.find { it.id == postId }
    }
}