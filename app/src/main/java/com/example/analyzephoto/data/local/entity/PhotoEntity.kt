package com.example.analyzephoto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey val id: String,
    val name: String,
    val filePath: String,
    val creationDate: Long
)