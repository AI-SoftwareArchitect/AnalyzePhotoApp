package com.example.analyzephoto.presentation.album

sealed class AlbumIntent {
    object LoadPhotos : AlbumIntent()
    data class SearchPhotos(val query: String) : AlbumIntent()
    data class FilterByDate(val dateRange: DateRange) : AlbumIntent()
    data class FilterByEditMode(val editMode: EditMode?) : AlbumIntent()
    object ClearFilters : AlbumIntent()
    data class SelectPhoto(val photoId: String) : AlbumIntent()
    data class DeletePhoto(val photoId: String) : AlbumIntent()
    data class SharePhoto(val photoId: String) : AlbumIntent()
    object ToggleViewMode : AlbumIntent()
    object RefreshPhotos : AlbumIntent()
    data class OnPhotosLoaded(val photos: List<PhotoItem>) : AlbumIntent()
    data class OnError(val error: String) : AlbumIntent()
}

data class PhotoItem(
    val id: String,
    val name: String,
    val uri: String,
    val thumbnailUri: String,
    val createdAt: Long,
    val editMode: EditMode,
    val size: String,
    val improvements: List<String>
)

enum class EditMode {
    CLEAN_MODE,
    FIX_MODE
}

enum class ViewMode {
    GRID,
    LIST
}

data class DateRange(
    val startDate: Long,
    val endDate: Long
)