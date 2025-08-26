package com.example.analyzephoto.presentation.album

data class AlbumState(
    val isLoading: Boolean = false,
    val photos: List<PhotoItem> = emptyList(),
    val filteredPhotos: List<PhotoItem> = emptyList(),
    val searchQuery: String = "",
    val selectedDateRange: DateRange? = null,
    val selectedEditMode: EditMode? = null,
    val viewMode: ViewMode = ViewMode.GRID,
    val selectedPhotoId: String? = null,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val showFilters: Boolean = false,
    val totalPhotos: Int = 0,
    val sortOrder: SortOrder = SortOrder.DATE_DESC
)

enum class SortOrder {
    DATE_ASC,
    DATE_DESC,
    NAME_ASC,
    NAME_DESC,
    SIZE_ASC,
    SIZE_DESC
}