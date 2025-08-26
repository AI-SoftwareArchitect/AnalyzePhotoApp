package com.example.analyzephoto.domain.model

data class Photo(
    val id: String,
    val name: String,
    val filePath: String,
    val creationDate: Long
)