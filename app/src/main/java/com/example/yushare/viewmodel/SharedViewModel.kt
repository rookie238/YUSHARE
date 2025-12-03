package com.example.yushare.viewmodel

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.yushare.model.Course
import com.example.yushare.model.Post
import com.google.firebase.auth.FirebaseAuth // AUTH KÜTÜPHANESİ EKLENDİ
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class SharedViewModel : ViewModel() {

    var coursesList = mutableStateListOf<Course>()
        private set
    var postsList = mutableStateListOf<Post>()
        private set
    var selectedCoursePosts = mutableStateListOf<Post>()
        private set

    var isUploading = mutableStateOf(false)

    init {
        try {
            fetchCourses()
            fetchPosts()
        } catch (e: Exception) { }
    }

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

    // --- İŞTE SENİN SORDUĞUN FONKSİYON BURAYA GELDİ ---
    private fun getCurrentUsername(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return when {
            user == null -> "@misafir"
            !user.displayName.isNullOrEmpty() -> user.displayName!! // İsim varsa (Google girişlerinde olur)
            !user.email.isNullOrEmpty() -> "@${user.email!!.substringBefore("@")}" // Yoksa mailin başını al
            else -> "@kullanici"
        }
    }

    // --- UPLOAD FONKSİYONU GÜNCELLENDİ ---
    fun uploadPost(fileUri: Uri?, courseCode: String, description: String, context: Context, onSuccess: () -> Unit) {
        isUploading.value = true

        // Kullanıcı adını alıyoruz
        val currentUsername = getCurrentUsername()

        val db = FirebaseFirestore.getInstance()

        // 1. Dosya Yoksa (Sadece Yazı)
        if (fileUri == null) {
            val postMap = hashMapOf<String, Any>(
                "username" to currentUsername, // ARTIK OTOMATİK
                "courseCode" to courseCode,
                "fileUrl" to "",
                "fileType" to "text",
                "description" to description,
                "timestamp" to System.currentTimeMillis()
            )
            saveToFirestore(postMap, context, onSuccess)
            return
        }

        // 2. Dosya Varsa
        val fileType = getFileType(context, fileUri)
        val storageRef = FirebaseStorage.getInstance().reference
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(fileUri)) ?: "file"
        val fileName = "uploads/${UUID.randomUUID()}.$extension"
        val fileRef = storageRef.child(fileName)

        fileRef.putFile(fileUri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                val postMap = hashMapOf<String, Any>(
                    "username" to currentUsername, // ARTIK OTOMATİK
                    "courseCode" to courseCode,
                    "fileUrl" to uri.toString(),
                    "fileType" to fileType,
                    "description" to description,
                    "timestamp" to System.currentTimeMillis()
                )
                saveToFirestore(postMap, context, onSuccess)
            }
        }.addOnFailureListener {
            isUploading.value = false
            Toast.makeText(context, "Yükleme Hatası: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveToFirestore(data: HashMap<String, Any>, context: Context, onSuccess: () -> Unit) {
        FirebaseFirestore.getInstance().collection("uploads").add(data).addOnSuccessListener {
            isUploading.value = false
            Toast.makeText(context, "Paylaşıldı!", Toast.LENGTH_SHORT).show()
            fetchPosts()
            onSuccess()
        }.addOnFailureListener {
            isUploading.value = false
            Toast.makeText(context, "Veritabanı Hatası: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}