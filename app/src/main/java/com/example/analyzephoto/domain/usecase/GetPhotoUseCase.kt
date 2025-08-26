package com.example.analyzephoto.domain.usecase

import com.example.analyzephoto.domain.model.Photo
import com.example.analyzephoto.data.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow

class GetPhotosUseCase @Inject constructor(private val repository: PhotoRepository) {
    operator fun invoke(): Flow<List<Photo>> {
        return repository.getPhotos()
    }
}