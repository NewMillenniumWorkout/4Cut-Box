package com.example.a4cut_box.model

data class Element(
    val id: String = "",
    val uid: String = "",
    val imageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val loadAddress: String = "",
    val memo: String = "",
    val tags: List<String> = listOf(),
)
