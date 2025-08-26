package com.example.analyzephoto.presentation.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    // private val getPhotosUseCase: GetPhotosUseCase,
    // private val deletePhotoUseCase: DeletePhotoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AlbumState())
    val state: StateFlow<AlbumState> = _state.asStateFlow()

    private val _effect = Channel<AlbumEffect>()
    val effect: Flow<AlbumEffect> = _effect.receiveAsFlow()

    init {
        handleIntent(AlbumIntent.LoadPhotos)
    }

    fun handleIntent(intent: AlbumIntent) {
        when (intent) {
            is AlbumIntent.LoadPhotos -> {
                loadPhotos()
            }
            is AlbumIntent.SearchPhotos -> {
                searchPhotos(intent.query)
            }
            is AlbumIntent.FilterByDate -> {
                filterByDate(intent.dateRange)
            }
            is AlbumIntent.FilterByEditMode -> {
                filterByEditMode(intent.editMode)
            }
            is AlbumIntent.ClearFilters -> {
                clearFilters()
            }
            is AlbumIntent.SelectPhoto -> {
                selectPhoto(intent.photoId)
            }
            is AlbumIntent.DeletePhoto -> {
                deletePhoto(intent.photoId)
            }
            is AlbumIntent.SharePhoto -> {
                sharePhoto(intent.photoId)
            }
            is AlbumIntent.ToggleViewMode -> {
                toggleViewMode()
            }
            is AlbumIntent.RefreshPhotos -> {
                refreshPhotos()
            }
            is AlbumIntent.OnPhotosLoaded -> {
                onPhotosLoaded(intent.photos)
            }
            is AlbumIntent.OnError -> {
                handleError(intent.error)
            }
        }
    }

    private fun loadPhotos() {
        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                // Simulate loading photos
                kotlinx.coroutines.delay(1000)
                val mockPhotos = generateMockPhotos()

                _state.value = _state.value.copy(
                    isLoading = false,
                    photos = mockPhotos,
                    filteredPhotos = mockPhotos,
                    totalPhotos = mockPhotos.size
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load photos: ${e.message}"
                )
            }
        }
    }

    private fun searchPhotos(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        applyFilters()
    }

    private fun filterByDate(dateRange: DateRange) {
        _state.value = _state.value.copy(selectedDateRange = dateRange)
        applyFilters()
    }

    private fun filterByEditMode(editMode: EditMode?) {
        _state.value = _state.value.copy(selectedEditMode = editMode)
        applyFilters()
    }

    private fun clearFilters() {
        _state.value = _state.value.copy(
            searchQuery = "",
            selectedDateRange = null,
            selectedEditMode = null,
            filteredPhotos = _state.value.photos
        )
    }

    private fun applyFilters() {
        val currentState = _state.value
        var filtered = currentState.photos

        // Apply search filter
        if (currentState.searchQuery.isNotBlank()) {
            filtered = filtered.filter { photo ->
                photo.name.contains(currentState.searchQuery, ignoreCase = true)
            }
        }

        // Apply date filter
        currentState.selectedDateRange?.let { dateRange ->
            filtered = filtered.filter { photo ->
                photo.createdAt >= dateRange.startDate && photo.createdAt <= dateRange.endDate
            }
        }

        // Apply edit mode filter
        currentState.selectedEditMode?.let { editMode ->
            filtered = filtered.filter { photo ->
                photo.editMode == editMode
            }
        }

        // Apply sorting
        filtered = when (currentState.sortOrder) {
            SortOrder.DATE_ASC -> filtered.sortedBy { it.createdAt }
            SortOrder.DATE_DESC -> filtered.sortedByDescending { it.createdAt }
            SortOrder.NAME_ASC -> filtered.sortedBy { it.name }
            SortOrder.NAME_DESC -> filtered.sortedByDescending { it.name }
            SortOrder.SIZE_ASC -> filtered.sortedBy { it.size }
            SortOrder.SIZE_DESC -> filtered.sortedByDescending { it.size }
        }

        _state.value = _state.value.copy(filteredPhotos = filtered)
    }

    private fun selectPhoto(photoId: String) {
        _state.value = _state.value.copy(selectedPhotoId = photoId)
        viewModelScope.launch {
            _effect.send(AlbumEffect.ShowPhotoDetails(photoId))
        }
    }

    private fun deletePhoto(photoId: String) {
        viewModelScope.launch {
            try {
                // Simulate delete operation
                kotlinx.coroutines.delay(500)

                val updatedPhotos = _state.value.photos.filter { it.id != photoId }
                _state.value = _state.value.copy(
                    photos = updatedPhotos,
                    selectedPhotoId = null
                )
                applyFilters()

                _effect.send(AlbumEffect.ShowMessage("Photo deleted successfully"))
            } catch (e: Exception) {
                _effect.send(AlbumEffect.ShowError("Failed to delete photo: ${e.message}"))
            }
        }
    }

    private fun sharePhoto(photoId: String) {
        viewModelScope.launch {
            val photo = _state.value.photos.find { it.id == photoId }
            photo?.let {
                _effect.send(AlbumEffect.SharePhoto(it.uri, it.name))
            }
        }
    }

    private fun toggleViewMode() {
        val newViewMode = when (_state.value.viewMode) {
            ViewMode.GRID -> ViewMode.LIST
            ViewMode.LIST -> ViewMode.GRID
        }
        _state.value = _state.value.copy(viewMode = newViewMode)
    }

    private fun refreshPhotos() {
        _state.value = _state.value.copy(isRefreshing = true)
        viewModelScope.launch {
            try {
                kotlinx.coroutines.delay(1500)
                val mockPhotos = generateMockPhotos()
                _state.value = _state.value.copy(
                    isRefreshing = false,
                    photos = mockPhotos,
                    totalPhotos = mockPhotos.size
                )
                applyFilters()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isRefreshing = false,
                    error = "Failed to refresh photos: ${e.message}"
                )
            }
        }
    }

    private fun onPhotosLoaded(photos: List<PhotoItem>) {
        _state.value = _state.value.copy(
            photos = photos,
            filteredPhotos = photos,
            totalPhotos = photos.size,
            isLoading = false
        )
    }

    private fun handleError(error: String) {
        _state.value = _state.value.copy(
            error = error,
            isLoading = false,
            isRefreshing = false
        )
    }

    fun toggleFilters() {
        _state.value = _state.value.copy(
            showFilters = !_state.value.showFilters
        )
    }

    fun setSortOrder(sortOrder: SortOrder) {
        _state.value = _state.value.copy(sortOrder = sortOrder)
        applyFilters()
    }

    private fun generateMockPhotos(): List<PhotoItem> {
        val currentTime = System.currentTimeMillis()
        return listOf(
            PhotoItem(
                id = "1",
                name = "Sunset Beach",
                createdAt = currentTime - 86400000L * 2, // 2 days ago
                size = 2048,
                editMode = EditMode.ORIGINAL,
                uri = "content://photos/sunset_beach"
            ),
            PhotoItem(
                id = "2",
                name = "Mountain View",
                createdAt = currentTime - 86400000L * 10, // 10 days ago
                size = 4096,
                editMode = EditMode.EDITED,
                uri = "content://photos/mountain_view"
            ),
            PhotoItem(
                id = "3",
                name = "City Lights",
                createdAt = currentTime - 86400000L * 5, // 5 days ago
                size = 1024,
                editMode = EditMode.ORIGINAL,
                uri = "content://photos/city_lights"
            ),
            PhotoItem(
                id = "4",
                name = "Forest Trail",
                createdAt = currentTime - 86400000L * 1, // 1 day ago
                size = 3072,
                editMode = EditMode.EDITED,
                uri = "content://photos/forest_trail"
            )
        )
    }
}

// Ek olarak PhotoItem ve diğer gerekli sınıfların örnek tanımları:

data class PhotoItem(
    val id: String,
    val name: String,
    val createdAt: Long,
    val size: Int, // Boyut KB veya byte cinsinden
    val editMode: EditMode,
    val uri: String
)

data class AlbumState(
    val photos: List<PhotoItem> = emptyList(),
    val filteredPhotos: List<PhotoItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedDateRange: DateRange? = null,
    val selectedEditMode: EditMode? = null,
    val selectedPhotoId: String? = null,
    val showFilters: Boolean = false,
    val sortOrder: SortOrder = SortOrder.DATE_DESC,
    val viewMode: ViewMode = ViewMode.GRID,
    val totalPhotos: Int = 0
)

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

sealed class AlbumEffect {
    data class ShowMessage(val message: String) : AlbumEffect()
    data class ShowError(val error: String) : AlbumEffect()
    data class ShowPhotoDetails(val photoId: String) : AlbumEffect()
    data class SharePhoto(val uri: String, val name: String) : AlbumEffect()
}

data class DateRange(
    val startDate: Long,
    val endDate: Long
)

enum class EditMode {
    ORIGINAL, EDITED
}

enum class SortOrder {
    DATE_ASC, DATE_DESC,
    NAME_ASC, NAME_DESC,
    SIZE_ASC, SIZE_DESC
}

enum class ViewMode {
    GRID, LIST
}
