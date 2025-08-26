package com.example.analyzephoto.presentation.analyze

data class AnalyzeState(
    val isLoading: Boolean = false,
    val originalPhotoUri: String? = null,
    val processedPhotoUri: String? = null,
    val photoName: String = "",
    val selectedEditMode: EditMode = EditMode.CLEAN_MODE,
    val isAnalyzing: Boolean = false,
    val isSaving: Boolean = false,
    val analysisResult: AnalysisResult? = null,
    val showBeforeAfter: Boolean = false,
    val progress: Float = 0f,
    val error: String? = null,
    val isPhotoSaved: Boolean = false
)