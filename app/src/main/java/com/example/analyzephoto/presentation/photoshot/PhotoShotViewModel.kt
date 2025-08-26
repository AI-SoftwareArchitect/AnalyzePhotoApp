package com.example.analyzephoto.presentation.photoshot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoShotViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(PhotoShotState())
    val state: StateFlow<PhotoShotState> = _state.asStateFlow()

    private val _effect = Channel<PhotoShotEffect>()
    val effect: Flow<PhotoShotEffect> = _effect.receiveAsFlow()

    fun handleIntent(intent: PhotoShotIntent) {
        when (intent) {
            is PhotoShotIntent.InitializeCamera -> {
                initializeCamera()
            }
            is PhotoShotIntent.TakePhoto -> {
                takePhoto()
            }
            is PhotoShotIntent.SwitchCamera -> {
                switchCamera()
            }
            is PhotoShotIntent.ApplyFilter -> {
                applyFilter(intent.filterType)
            }
            is PhotoShotIntent.RemoveFilter -> {
                removeFilter()
            }
            is PhotoShotIntent.ToggleFlash -> {
                toggleFlash()
            }
            is PhotoShotIntent.OnPhotoTaken -> {
                onPhotoTaken(intent.photoUri)
            }
            is PhotoShotIntent.OnError -> {
                handleError(intent.error)
            }
        }
    }

    private fun initializeCamera() {
        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                // Simulating camera initialization
                kotlinx.coroutines.delay(1000)
                _state.value = _state.value.copy(
                    isLoading = false,
                    isCameraReady = true,
                    permissionGranted = true
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Camera initialization failed: ${e.message}"
                )
            }
        }
    }

    private fun takePhoto() {
        if (!_state.value.isCameraReady) return

        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                // Simulating photo capture
                kotlinx.coroutines.delay(500)
                val photoUri = "content://photo_${System.currentTimeMillis()}.jpg"
                _state.value = _state.value.copy(
                    isLoading = false,
                    capturedPhotoUri = photoUri
                )
                _effect.send(PhotoShotEffect.NavigateToAnalyze(photoUri))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to capture photo: ${e.message}"
                )
            }
        }
    }

    private fun switchCamera() {
        _state.value = _state.value.copy(
            isFrontCamera = !_state.value.isFrontCamera
        )
    }

    private fun applyFilter(filterType: String) {
        _state.value = _state.value.copy(
            currentFilter = filterType
        )
    }

    private fun removeFilter() {
        _state.value = _state.value.copy(
            currentFilter = null
        )
    }

    private fun toggleFlash() {
        _state.value = _state.value.copy(
            isFlashOn = !_state.value.isFlashOn
        )
    }

    private fun onPhotoTaken(photoUri: String) {
        _state.value = _state.value.copy(
            capturedPhotoUri = photoUri
        )
    }

    private fun handleError(error: String) {
        _state.value = _state.value.copy(
            error = error,
            isLoading = false
        )
    }
}

sealed class PhotoShotEffect {
    data class NavigateToAnalyze(val photoUri: String) : PhotoShotEffect()
    data class ShowError(val message: String) : PhotoShotEffect()
}