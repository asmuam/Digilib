package com.polstat.digilib

import LoginScreen
import RegisterScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.polstat.digilib.ui.screen.BookCollectionScreen
import com.polstat.digilib.ui.screen.WelcomeScreen

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "welcome_screen"
    ) {
        composable("welcome_screen") {
            WelcomeScreen(navController)
        }
        composable("book_collection_screen") {
            BookCollectionScreen()
        }
        composable("login_screen") {
            LoginScreen(navController)
        }
        composable("register_screen") {
            RegisterScreen(navController)
        }
    }
}