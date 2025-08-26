package com.example.analyzephoto.presentation.analyze

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyzePhotoScreen(
    photoUri: String,
    onNavigateToPhotoShot: () -> Unit,
    onNavigateToAlbum: () -> Unit,
    viewModel: AnalyzeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(photoUri) {
        viewModel.handleIntent(AnalyzeIntent.LoadPhoto(photoUri))
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AnalyzeEffect.NavigateToPhotoShot -> onNavigateToPhotoShot()
                is AnalyzeEffect.NavigateToAlbum -> onNavigateToAlbum()
                is AnalyzeEffect.ShowAnalysisComplete -> {
                    // Show success animation or toast
                }
                is AnalyzeEffect.ShowError -> {
                    // Show error snackbar
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.Black.copy(alpha = 0.9f)
                    )
                )
            )
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Analyze Photo",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { viewModel.handleIntent(AnalyzeIntent.RetakePhoto) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Photo Preview
            item {
                PhotoPreviewSection(
                    originalUri = state.originalPhotoUri,
                    processedUri = state.processedPhotoUri,
                    showBeforeAfter = state.showBeforeAfter,
                    isLoading = state.isLoading,
                    onToggleBeforeAfter = { viewModel.toggleBeforeAfter() }
                )
            }

            // Photo Name Input
            item {
                PhotoNameSection(
                    photoName = state.photoName,
                    onNameChanged = { viewModel.handleIntent(AnalyzeIntent.SetPhotoName(it)) }
                )
            }

            // Edit Mode Selection
            item {
                EditModeSection(
                    selectedMode = state.selectedEditMode,
                    onModeSelected = { viewModel.handleIntent(AnalyzeIntent.SelectEditMode(it)) },
                    enabled = !state.isAnalyzing
                )
            }

            // Analysis Progress
            if (state.isAnalyzing) {
                item {
                    AnalysisProgressSection(
                        progress = state.progress,
                        selectedMode = state.selectedEditMode
                    )
                }
            }

            // Analysis Results
            state.analysisResult?.let { result ->
                item {
                    AnalysisResultSection(result = result)
                }
            }

            // Action Buttons
            item {
                ActionButtonsSection(
                    canAnalyze = state.originalPhotoUri != null && !state.isAnalyzing,
                    canSave = state.processedPhotoUri != null && !state.isSaving,
                    isAnalyzing = state.isAnalyzing,
                    isSaving = state.isSaving,
                    onAnalyze = { viewModel.handleIntent(AnalyzeIntent.AnalyzePhoto) },
                    onSave = { viewModel.handleIntent(AnalyzeIntent.SavePhoto) }
                )
            }
        }

        // Error Snackbar
        state.error?.let { error ->
            LaunchedEffect(error) {
                // Show snackbar
            }
        }
    }
}

@Composable
private fun PhotoPreviewSection(
    originalUri: String?,
    processedUri: String?,
    showBeforeAfter: Boolean,
    isLoading: Boolean,
    onToggleBeforeAfter: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.3f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        color = Color(0xFFFFD700),
                        strokeWidth = 3.dp
                    )
                }
                originalUri != null -> {
                    // Photo preview would go here
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (showBeforeAfter && processedUri != null) "Processed Image" else "Original Image",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            if (processedUri != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = onToggleBeforeAfter,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFFD700).copy(alpha = 0.2f)
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text(
                                        text = if (showBeforeAfter) "Show Original" else "Show Processed",
                                        color = Color(0xFFFFD700),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
                else -> {
                    Text(
                        text = "No photo loaded",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotoNameSection(
    photoName: String,
    onNameChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Photo Name",
                color = Color(0xFFFFD700),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = photoName,
                onValueChange = onNameChanged,
                placeholder = {
                    Text(
                        text = "Enter photo name (optional)",
                        color = Color.White.copy(alpha = 0.6f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFFFD700),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    cursorColor = Color(0xFFFFD700)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "If left empty, a random name will be generated",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun EditModeSection(
    selectedMode: EditMode,
    onModeSelected: (EditMode) -> Unit,
    enabled: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Edit Mode",
                color = Color(0xFFFFD700),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EditModeChip(
                    mode = EditMode.CLEAN_MODE,
                    label = "Clean Mode",
                    description = "Noise reduction & optimization",
                    isSelected = selectedMode == EditMode.CLEAN_MODE,
                    enabled = enabled,
                    onClick = { onModeSelected(EditMode.CLEAN_MODE) },
                    modifier = Modifier.weight(1f)
                )
                EditModeChip(
                    mode = EditMode.FIX_MODE,
                    label = "Fix Mode",
                    description = "Portrait enhancements",
                    isSelected = selectedMode == EditMode.FIX_MODE,
                    enabled = enabled,
                    onClick = { onModeSelected(EditMode.FIX_MODE) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EditModeChip(
    mode: EditMode,
    label: String,
    description: String,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(enabled = enabled) { onClick() }
            .border(
                2.dp,
                if (isSelected) Color(0xFFFFD700) else Color.Transparent,
                RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                Color(0xFFFFD700).copy(alpha = 0.2f)
            else
                Color.Black.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (mode == EditMode.CLEAN_MODE) Icons.Default.AutoFixHigh else Icons.Default.Face,
                contentDescription = label,
                tint = if (isSelected) Color(0xFFFFD700) else Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = if (isSelected) Color(0xFFFFD700) else Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
private fun AnalysisProgressSection(
    progress: Float,
    selectedMode: EditMode
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Analyzing Photo...",
                color = Color(0xFFFFD700),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = progress,
                    color = Color(0xFFFFD700),
                    strokeWidth = 4.dp,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = when (selectedMode) {
                    EditMode.CLEAN_MODE -> "Applying noise reduction and optimization..."
                    EditMode.FIX_MODE -> "Enhancing portrait features..."
                },
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun AnalysisResultSection(
    result: AnalysisResult
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color.Green,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Analysis Complete",
                    color = Color(0xFFFFD700),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Improvements Applied:",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            result.improvements.forEach { improvement ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Applied",
                        tint = Color.Green,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = improvement,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip(
                    label = "Processing Time",
                    value = "${result.processingTime / 1000}s"
                )
                InfoChip(
                    label = "Size Reduction",
                    value = "${result.originalSize} → ${result.processedSize}"
                )
            }
        }
    }
}

@Composable
private fun InfoChip(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                Color.Black.copy(alpha = 0.3f),
                RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 10.sp
        )
        Text(
            text = value,
            color = Color(0xFFFFD700),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ActionButtonsSection(
    canAnalyze: Boolean,
    canSave: Boolean,
    isAnalyzing: Boolean,
    isSaving: Boolean,
    onAnalyze: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Analyze Button
        Button(
            onClick = onAnalyze,
            enabled = canAnalyze,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700).copy(alpha = 0.2f),
                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(16.dp),
            border = if (canAnalyze) BorderStroke(1.dp, Color(0xFFFFD700)) else null
        ) {
            if (isAnalyzing) {
                CircularProgressIndicator(
                    color = Color(0xFFFFD700),
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Analyze",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ANALYZE",
                        color = Color(0xFFFFD700),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Save Button
        Button(
            onClick = onSave,
            enabled = canSave,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700),
                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SAVE",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}