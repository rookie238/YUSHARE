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
import com.example.yushare.model.Course
import com.example.yushare.model.Post
import com.example.yushare.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

    // Yükleme yapılıyor mu? (Dönme efekti için)
    var isUploading = mutableStateOf(false)

    // Uygulama açılınca dersleri ve gönderileri çek
    init {
        try {
            fetchCourses()
            fetchPosts()
            fetchUserProfile() // Uygulama açılır açılmaz profili de çeksin
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

    // --- PROFİL VERİSİNİ ÇEKME (GÜNCELLENDİ) ---
    fun fetchUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Kullanıcının UID'si ile dökümanı buluyoruz
            // DİKKAT: Koleksiyon adı 'Users' (Büyük harfle başlıyorsa dikkat et)
            db.collection("Users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        currentUserProfile.value = UserProfile(
                            name = document.getString("name") ?: "İsimsiz",
                            studentId = document.getString("id") ?: "",
                            department = document.getString("department") ?: "Bölüm Girilmemiş",
                            degree = document.getString("degree") ?: "Lisans",
                            // YENİ EKLENEN ALAN: BIO
                            bio = document.getString("bio") ?: ""
                        )
                    }
                }
        }
    }

    // --- PROFİLİ GÜNCELLEME (YENİ) ---
    // Bu fonksiyonu "Profili Düzenle" ekranında kullanacağız
    fun updateUserProfile(newName: String, newBio: String, onResult: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        val updates = hashMapOf<String, Any>(
            "name" to newName,
            "bio" to newBio
        )

        db.collection("Users").document(userId) // Koleksiyon adı 'Users'
            .update(updates)
            .addOnSuccessListener {
                // Ekrandaki veriyi hemen güncelle (Tekrar fetch yapmaya gerek kalmaz)
                currentUserProfile.value = currentUserProfile.value.copy(
                    name = newName,
                    bio = newBio
                )
                onResult(true) // Başarılı
            }
            .addOnFailureListener {
                onResult(false) // Başarısız
            }
    }

    // --- GÖNDERİLERİ ÇEKME ---
    fun fetchPosts() {
        db.collection("uploads")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                postsList.clear()
                for (document in result) {
                    postsList.add(mapDocumentToPost(document))
                }
            }
    }

    // Kullanıcının mail adresini getiren fonksiyon
    fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: "Mail adresi yok"
    }

    // Belirli bir dersin gönderilerini çekme
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
            username = document.getString("username") ?: "@anonim",
            courseCode = document.getString("courseCode") ?: "",
            fileUrl = url,
            description = document.getString("description") ?: "",
            fileType = document.getString("fileType") ?: "image"
        )
    }

    // Helper: Dosya Tipini Anlama
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

    // Helper: Kullanıcı Adını Alma
    fun getCurrentUsername(): String {
        val user = auth.currentUser
        return when {
            user == null -> "@misafir"
            !user.displayName.isNullOrEmpty() -> user.displayName!!
            !user.email.isNullOrEmpty() -> "@${user.email!!.substringBefore("@")}"
            else -> "@kullanici"
        }
    }

    // --- YÜKLEME İŞLEMİ (Cloudinary) ---
    fun uploadPost(fileUri: Uri?, courseCode: String, description: String, context: Context, onSuccess: () -> Unit) {
        isUploading.value = true
        val currentUsername = getCurrentUsername()

        // DURUM 1: Sadece Yazı Var
        if (fileUri == null) {
            val postMap = hashMapOf<String, Any>(
                "username" to currentUsername,
                "courseCode" to courseCode,
                "fileUrl" to "",
                "fileType" to "text",
                "description" to description,
                "timestamp" to System.currentTimeMillis()
            )
            saveToFirestore(postMap, context, onSuccess)
            return
        }

        // DURUM 2: Dosya Var -> Cloudinary
        val fileType = getFileType(context, fileUri)

        MediaManager.get().upload(fileUri)
            .unsigned("ml_default")
            .option("resource_type", "auto")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    val downloadUrl = resultData?.get("secure_url").toString()

                    val postMap = hashMapOf<String, Any>(
                        "username" to currentUsername,
                        "courseCode" to courseCode,
                        "fileUrl" to downloadUrl,
                        "fileType" to fileType,
                        "description" to description,
                        "timestamp" to System.currentTimeMillis()
                    )
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

    // Helper: Firestore'a Yazma
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
}