package com.example.a4cut_box.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class FeatureViewModel : ViewModel() {
    private val _elements = MutableStateFlow<List<Element>>(emptyList())
    val elements = _elements.asStateFlow()

    private val _isUploading = MutableStateFlow(false) // 업로드 상태
    val isUploading = _isUploading.asStateFlow()

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()

    fun sendElement(
        imageUri: String, roadAddress: String, latitude: Double,
        longitude: Double, memo: String, tags: List<String>
    ) {
        val uid = firebaseAuth.currentUser?.uid ?: ""
        if (uid.isEmpty()) {
            Log.e("FeatureViewModel", "User is not authenticated")
            return
        }

        _isUploading.value = true // 업로드 시작

        val storageRef = firebaseStorage.reference
        val storageFileName = "${UUID.randomUUID()}.jpg"
        val storageReference = storageRef.child(storageFileName)

        val uploadTask = storageReference.putFile(Uri.parse(imageUri))
        uploadTask.addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()

                val element = Element(
                    id = firebaseDatabase.reference.child("element").push().key ?: UUID.randomUUID()
                        .toString(),
                    uid = uid,
                    imageUrl = downloadUrl,
                    roadAddress = roadAddress,
                    latitude = latitude,
                    longitude = longitude,
                    memo = memo,
                    tags = tags
                )

                firebaseDatabase.reference.child("element").child(uid).push().setValue(element)
                    .addOnCompleteListener { event ->
                        _isUploading.value = false // 업로드 종료
                        if (event.isSuccessful) {
                            Log.d("FeatureViewModel", "Element successfully added to database")
                        } else {
                            Log.e(
                                "FeatureViewModel",
                                "Failed to add element to database: ${event.exception}"
                            )
                        }
                    }
            }.addOnFailureListener { e ->
                _isUploading.value = false // 업로드 종료
                Log.e("FeatureViewModel", "Failed to retrieve download URL: $e")
            }
        }.addOnFailureListener { e ->
            _isUploading.value = false // 업로드 종료
            Log.e("FeatureViewModel", "Failed to upload image to Firebase Storage: $e")
        }
    }

    fun listenForElement() {
        val uid = firebaseAuth.currentUser?.uid ?: ""
        if (uid.isEmpty()) {
            Log.e("FeatureViewModel", "User is not authenticated")
            return
        }

        firebaseDatabase.reference.child("element").child(uid).orderByChild("createAt")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Element>()
                    snapshot.children.forEach { data ->
                        val element = data.getValue(Element::class.java)
                        element?.let { list.add(it) }
                    }
                    _elements.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Error: ${error.message}")
                }
            })
    }
}