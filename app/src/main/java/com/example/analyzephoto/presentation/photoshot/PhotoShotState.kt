package com.example.analyzephoto.presentation.photoshot

data class PhotoShotState(
    val isLoading: Boolean = false,
    val isCameraReady: Boolean = false,
    val currentFilter: String? = null,
    val isFlashOn: Boolean = false,
    val isFrontCamera: Boolean = false,
    val capturedPhotoUri: String? = null,
    val availableFilters: List<String> = listOf("dog", "cat", "bunny", "crown", "glasses"),
    val error: String? = null,
    val permissionGranted: Boolean = false
)