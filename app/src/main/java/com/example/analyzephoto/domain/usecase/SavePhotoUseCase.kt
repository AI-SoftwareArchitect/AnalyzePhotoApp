package com.example.analyzephoto.domain.usecase

import com.example.photoeditor.domain.model.Photo
import com.example.photoeditor.data.repository.PhotoRepository

class SavePhotoUseCase @Inject constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(photo: Photo) {
        repository.savePhoto(photo)
    }
}
