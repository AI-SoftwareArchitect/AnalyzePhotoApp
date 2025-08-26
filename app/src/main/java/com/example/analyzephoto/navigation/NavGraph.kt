package com.example.analyzephoto.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.analyzephoto.presentation.album.AlbumScreen
import com.example.analyzephoto.presentation.analyze.AnalyzePhotoScreen
import com.example.analyzephoto.presentation.login.LoginScreen
import com.example.analyzephoto.presentation.register.RegisterScreen

@Composable
fun PhotoEditorNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("photoShot") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("login") },
                navController = TODO(),
                viewModel = TODO()
            )
        }
        composable("photoShot") {
            PhotoShotScreen(onPhotoTaken = { uri ->
                navController.navigate("analyze/${uri.toString()}")
            })
        }
        composable("analyze/{photoUri}") { backStackEntry ->
            val uri = backStackEntry.arguments?.getString("photoUri") ?: ""
            AnalyzePhotoScreen(
                photoUri = uri, onSaveComplete = {
                    navController.navigate("album")
                },
                onNavigateToPhotoShot = TODO(),
                onNavigateToAlbum = TODO(),
                viewModel = TODO()
            )
        }
        composable("album") {
            AlbumScreen(
                viewModel = TODO(),
                onPhotoSelected = TODO()
            )
        }
    }
}