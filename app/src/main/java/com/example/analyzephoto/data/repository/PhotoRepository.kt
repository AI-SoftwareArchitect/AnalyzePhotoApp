package com.example.analyzephoto.data.repository
import com.example.analyzephoto.data.local.dao.PhotoDao
import com.example.analyzephoto.data.local.entity.PhotoEntity
import com.example.analyzephoto.domain.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val photoDao: PhotoDao) {

    fun getPhotos(): Flow<List<Photo>> {
        return photoDao.getPhotos().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun savePhoto(photo: Photo) {
        photoDao.insertPhoto(photo.toEntity())
    }

    suspend fun deletePhoto(id: String) {
        photoDao.deletePhoto(id)
    }

    private fun PhotoEntity.toDomain(): Photo {
        return Photo(
            id = id,
            name = name,
            filePath = filePath,
            creationDate = creationDate
        )
    }

    private fun Photo.toEntity(): PhotoEntity {
        return PhotoEntity(
            id = id,
            name = name,
            filePath = filePath,
            creationDate = creationDate
        )
    }
}
