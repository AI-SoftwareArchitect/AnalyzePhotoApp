package com.example.analyzephoto.presentation.album

data class PhotoItem(
    val id: String,
    val name: String,
    val uri: String,
    val thumbnailUri: String,
    val createdAt: Long,
    val editMode: EditMode,
    val size: String,
    val improvements: List<String>
)
