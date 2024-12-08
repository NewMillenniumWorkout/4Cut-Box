package com.example.a4cut_box.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class FeatureViewModel : ViewModel() {
    private val _elements = MutableStateFlow<List<Element>>(emptyList())
    val elements = _elements.asStateFlow()

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun sendElement() {
        val uid = firebaseAuth.currentUser?.uid ?: ""
        val element = Element(
            id = firebaseDatabase.reference.child("element").push().key ?: UUID.randomUUID()
                .toString(),
            uid = uid,
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/fourcutbox.firebasestorage.app/o/KakaoTalk_Photo_2024-11-30-23-46-08.jpeg?alt=media&token=533282cb-cc51-4384-8a09-58c54f0a24ab",
            loadAddress = "서울특별시 관악구 조원로 25",
            memo = "오늘도 날이 좋구나!",
            tags = listOf("포항", "테스트_데이터", "hello!")
        )

        firebaseDatabase.reference.child("element").child(uid).push().setValue(element)
            .addOnCompleteListener { event ->
                Log.d("me", event.toString())
            }
    }

    fun listenForElement() {
        val uid = firebaseAuth.currentUser?.uid ?: ""
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