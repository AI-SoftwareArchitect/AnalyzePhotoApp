package com.example.analyzephoto.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.analyzephoto.data.local.dao.PhotoDao
import com.example.analyzephoto.domain.model.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}