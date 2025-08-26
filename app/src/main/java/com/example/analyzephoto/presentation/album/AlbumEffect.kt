package com.example.analyzephoto.presentation.album

sealed class AlbumEffect {
    data class ShowMessage(val message: String) : AlbumEffect()
    data class ShowError(val error: String) : AlbumEffect()
    data class ShowPhotoDetails(val photoId: String) : AlbumEffect()
    data class SharePhoto(val uri: String, val name: String) : AlbumEffect()
}
