package com.example.analyzephoto.presentation.album

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.analyzephoto.presentation.theme.Gold
import com.example.analyzephoto.presentation.theme.Black
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AlbumScreen(
    viewModel: AlbumViewModel = hiltViewModel(),
    onPhotoSelected: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()

    // Listen for one-time effects
    LaunchedEffect(key1 = true) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AlbumEffect.ShowMessage -> {
                    scaffoldState.snackbarHostState.showSnackbar(effect.message)
                }
                is AlbumEffect.ShowError -> {
                    scaffoldState.snackbarHostState.showSnackbar("Error: ${effect.error}")
                }
                is AlbumEffect.ShowPhotoDetails -> {
                    onPhotoSelected(effect.photoId)
                }
                // Add other effects if needed
                else -> {}
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Album", color = Gold) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Black
                )
            )
        },
        containerColor = Black
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Black)
        ) {
            SearchBar(
                query = state.searchQuery,
                onQueryChanged = { viewModel.handleIntent(AlbumIntent.SearchPhotos(it)) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    color = Gold
                )
            } else if (state.error != null) {
                Text(
                    text = state.error ?: "",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                PhotoList(
                    photos = state.filteredPhotos,
                    onPhotoClick = { photoId ->
                        viewModel.handleIntent(AlbumIntent.SelectPhoto(photoId))
                    },
                    onDeleteClick = { photoId ->
                        viewModel.handleIntent(AlbumIntent.DeletePhoto(photoId))
                    }
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text(text = "Search photos", color = Gold.copy(alpha = 0.6f)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = Gold
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Black,
            textColor = Gold,
            cursorColor = Gold,
            focusedIndicatorColor = Gold,
            unfocusedIndicatorColor = Gold.copy(alpha = 0.5f),
            placeholderColor = Gold.copy(alpha = 0.6f)
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun PhotoList(
    photos: List<PhotoItem>,
    onPhotoClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(photos, key = { it.id }) { photo ->
            PhotoListItem(
                photo = photo,
                onClick = { onPhotoClick(photo.id) },
                onDeleteClick = { onDeleteClick(photo.id) }
            )
        }
    }
}

@Composable
fun PhotoListItem(
    photo: PhotoItem,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Example gradient
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Gold.copy(alpha = 0.8f),
            Gold.copy(alpha = 0.4f)
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .background(Color.Black)
            .animateContentSize(animationSpec = TweenSpec(durationMillis = 300)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder for photo thumbnail
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(gradient)
        ) {
            // Ideally load with Coil or similar
            // For example purposes, show icon placeholder
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_camera),
                contentDescription = photo.name,
                tint = Gold,
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = photo.name,
                color = Gold,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Size: ${photo.size} KB",
                color = Gold.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = android.text.format.DateFormat.format("dd MMM yyyy", photo.createdAt).toString(),
                color = Gold.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }

        IconButton(
            onClick = onDeleteClick
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete photo",
                tint = Gold
            )
        }
    }
}
