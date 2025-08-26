package com.example.analyzephoto.presentation.photoshot

sealed class PhotoShotIntent {
    data object InitializeCamera : PhotoShotIntent()
    data object TakePhoto : PhotoShotIntent()
    data object SwitchCamera : PhotoShotIntent()
    data class ApplyFilter(val filterType: String) : PhotoShotIntent()
    data object RemoveFilter : PhotoShotIntent()
    data object ToggleFlash : PhotoShotIntent()
    data class OnPhotoTaken(val photoUri: String) : PhotoShotIntent()
    data class OnError(val error: String) : PhotoShotIntent()
}