package com.example.analyzephoto.presentation.analyze

sealed class AnalyzeIntent {
    data class LoadPhoto(val photoUri: String) : AnalyzeIntent()
    data class SetPhotoName(val name: String) : AnalyzeIntent()
    data class SelectEditMode(val mode: EditMode) : AnalyzeIntent()
    data object AnalyzePhoto : AnalyzeIntent()
    data object SavePhoto : AnalyzeIntent()
    data object RetakePhoto : AnalyzeIntent()
    data class OnAnalysisComplete(val result: AnalysisResult) : AnalyzeIntent()
    data class OnError(val error: String) : AnalyzeIntent()
}

enum class EditMode {
    CLEAN_MODE,
    FIX_MODE
}

data class AnalysisResult(
    val processedImageUri: String,
    val improvements: List<String>,
    val originalSize: String,
    val processedSize: String,
    val processingTime: Long
)