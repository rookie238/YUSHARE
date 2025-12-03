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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class SharedViewModel : ViewModel() {

    // --- LİSTELERİMİZ ---
    var coursesList = mutableStateListOf<Course>()
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
        } catch (e: Exception) { }
    }

    // --- ESKİ FONKSİYONLAR (AYNEN KALDI) ---
    private fun fetchCourses() {
        val db = FirebaseFirestore.getInstance()
        db.collection("courses").get().addOnSuccessListener { result ->
            coursesList.clear()
            for (document in result) {
                coursesList.add(Course(
                    document.id,
                    document.getString("Title") ?: "",
                    document.getString("Subtitle") ?: ""
                ))
            }
        }
    }

    fun fetchPosts() {
        val db = FirebaseFirestore.getInstance()
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

    fun fetchPostsByCourse(courseCode: String) {
        val db = FirebaseFirestore.getInstance()
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

    // Veritabanından gelen veriyi Post modeline çevirir
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

    // Dosyanın türünü (Resim mi, PDF mi?) anlar
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

    // Giriş yapan kullanıcının adını bulur
    private fun getCurrentUsername(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return when {
            user == null -> "@misafir"
            !user.displayName.isNullOrEmpty() -> user.displayName!!
            !user.email.isNullOrEmpty() -> "@${user.email!!.substringBefore("@")}"
            else -> "@kullanici"
        }
    }

    // --- YENİ BÖLÜM: YÜKLEME İŞLEMİ (Cloudinary) ---
    fun uploadPost(fileUri: Uri?, courseCode: String, description: String, context: Context, onSuccess: () -> Unit) {
        isUploading.value = true
        val currentUsername = getCurrentUsername()

        // DURUM 1: Sadece Yazı Var, Dosya Yok
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

        // DURUM 2: Dosya Var -> Cloudinary'ye Yolla
        val fileType = getFileType(context, fileUri)

        MediaManager.get().upload(fileUri)
            .unsigned("ml_default") // <--- BURASI ÇOK ÖNEMLİ (Aşağıda anlatacağım)
            .option("resource_type", "auto")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    // Cloudinary yükledi ve bize linki verdi
                    val downloadUrl = resultData?.get("secure_url").toString()

                    // Şimdi bu linki Firebase'e kaydediyoruz
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

    // Veritabanına kaydetme yardımcısı
    private fun saveToFirestore(data: HashMap<String, Any>, context: Context, onSuccess: () -> Unit) {
        FirebaseFirestore.getInstance().collection("uploads").add(data).addOnSuccessListener {
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