package com.example.analyzephoto.presentation.analyze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AnalyzeViewModel @Inject constructor(
    // private val savePhotoUseCase: SavePhotoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyzeState())
    val state: StateFlow<AnalyzeState> = _state.asStateFlow()

    private val _effect = Channel<AnalyzeEffect>()
    val effect: Flow<AnalyzeEffect> = _effect.receiveAsFlow()

    fun handleIntent(intent: AnalyzeIntent) {
        when (intent) {
            is AnalyzeIntent.LoadPhoto -> {
                loadPhoto(intent.photoUri)
            }
            is AnalyzeIntent.SetPhotoName -> {
                setPhotoName(intent.name)
            }
            is AnalyzeIntent.SelectEditMode -> {
                selectEditMode(intent.mode)
            }
            is AnalyzeIntent.AnalyzePhoto -> {
                analyzePhoto()
            }
            is AnalyzeIntent.SavePhoto -> {
                savePhoto()
            }
            is AnalyzeIntent.RetakePhoto -> {
                retakePhoto()
            }
            is AnalyzeIntent.OnAnalysisComplete -> {
                onAnalysisComplete(intent.result)
            }
            is AnalyzeIntent.OnError -> {
                handleError(intent.error)
            }
        }
    }

    private fun loadPhoto(photoUri: String) {
        _state.value = _state.value.copy(
            isLoading = true,
            originalPhotoUri = photoUri,
            error = null
        )

        viewModelScope.launch {
            try {
                // Simulate photo loading
                kotlinx.coroutines.delay(500)
                _state.value = _state.value.copy(
                    isLoading = false,
                    originalPhotoUri = photoUri
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load photo: ${e.message}"
                )
            }
        }
    }

    private fun setPhotoName(name: String) {
        _state.value = _state.value.copy(photoName = name)
    }

    private fun selectEditMode(mode: EditMode) {
        _state.value = _state.value.copy(selectedEditMode = mode)
    }

    private fun analyzePhoto() {
        if (_state.value.originalPhotoUri == null) return

        _state.value = _state.value.copy(
            isAnalyzing = true,
            progress = 0f,
            error = null
        )

        viewModelScope.launch {
            try {
                // Simulate analysis progress
                for (i in 1..10) {
                    kotlinx.coroutines.delay(200)
                    _state.value = _state.value.copy(progress = i / 10f)
                }

                val improvements = when (_state.value.selectedEditMode) {
                    EditMode.CLEAN_MODE -> listOf(
                        "Noise reduction applied",
                        "Brightness optimized",
                        "Color saturation enhanced"
                    )
                    EditMode.FIX_MODE -> listOf(
                        "Blemishes removed",
                        "Red-eye correction applied",
                        "Skin smoothing applied",
                        "Background blur enhanced"
                    )
                }

                val result = AnalysisResult(
                    processedImageUri = "content://processed_${System.currentTimeMillis()}.jpg",
                    improvements = improvements,
                    originalSize = "2.4 MB",
                    processedSize = "2.1 MB",
                    processingTime = 2000L
                )

                _state.value = _state.value.copy(
                    isAnalyzing = false,
                    processedPhotoUri = result.processedImageUri,
                    analysisResult = result,
                    progress = 1f
                )

                _effect.send(AnalyzeEffect.ShowAnalysisComplete)

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isAnalyzing = false,
                    progress = 0f,
                    error = "Analysis failed: ${e.message}"
                )
            }
        }
    }

    private fun savePhoto() {
        if (_state.value.processedPhotoUri == null) return

        _state.value = _state.value.copy(isSaving = true)

        viewModelScope.launch {
            try {
                // Simulate saving with 1 second circular indicator
                kotlinx.coroutines.delay(1000)

                val finalName = if (_state.value.photoName.isBlank()) {
                    UUID.randomUUID().toString()
                } else {
                    _state.value.photoName
                }

                // Here you would call savePhotoUseCase
                // savePhotoUseCase(finalName, _state.value.processedPhotoUri!!)

                _state.value = _state.value.copy(
                    isSaving = false,
                    isPhotoSaved = true,
                    photoName = finalName
                )

                _effect.send(AnalyzeEffect.NavigateToAlbum)

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = "Failed to save photo: ${e.message}"
                )
            }
        }
    }

    private fun retakePhoto() {
        viewModelScope.launch {
            _effect.send(AnalyzeEffect.NavigateToPhotoShot)
        }
    }

    private fun onAnalysisComplete(result: AnalysisResult) {
        _state.value = _state.value.copy(
            analysisResult = result,
            processedPhotoUri = result.processedImageUri,
            isAnalyzing = false
        )
    }

    private fun handleError(error: String) {
        _state.value = _state.value.copy(
            error = error,
            isLoading = false,
            isAnalyzing = false,
            isSaving = false
        )
    }

    fun toggleBeforeAfter() {
        _state.value = _state.value.copy(
            showBeforeAfter = !_state.value.showBeforeAfter
        )
    }
}

sealed class AnalyzeEffect {
    object NavigateToPhotoShot : AnalyzeEffect()
    object NavigateToAlbum : AnalyzeEffect()
    object ShowAnalysisComplete : AnalyzeEffect()
    data class ShowError(val message: String) : AnalyzeEffect()
}