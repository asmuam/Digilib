package com.polstat.digilib

import LoginScreen
import RegisterScreen
import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.polstat.digilib.ui.screen.BookCollectionScreen
import com.polstat.digilib.ui.screen.BookCollectionViewModel
import com.polstat.digilib.ui.screen.BookDetailScreen
import com.polstat.digilib.ui.screen.BookEntryScreen
import com.polstat.digilib.ui.screen.WelcomeScreen

@Composable
fun App() {
    val navController = rememberNavController()
    val activity = (LocalContext.current as Activity)
    // Create the BookCollectionViewModel at the App level
    val bookCollectionViewModel: BookCollectionViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "welcome_screen"
    ) {
        composable("welcome_screen") {
            WelcomeScreen(navController)
        }
        composable("book_collection_screen") {
            val modifier: Modifier = Modifier
            BookCollectionScreen(
                viewModel = bookCollectionViewModel,
                onBookClick = {
                    navController.navigate("books/${it.id}")
                },
                modifier,
                navController
            )
        }
        composable(
            "books/{bookid}",
            arguments = listOf(navArgument("bookid") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("bookid")
            // Use the existing instance of BookCollectionViewModel
            // Mendapatkan detail buku berdasarkan ID
            val book = bookCollectionViewModel.getBookById(id)

            // Memastikan book tidak null sebelum menampilkan detail buku
            if (book != null) {
                BookDetailScreen(
                    book = book,
                    onBackClick = { navController.navigateUp() },
                    onShareClick = {
                        createShareIntent(activity, it)
                    }
                )
            } else {
                // Handle kasus ketika buku dengan ID tertentu tidak ditemukan
                // Misalnya, tampilkan pesan atau navigasi ke halaman lain
                // sesuai kebutuhan aplikasi Anda.
            }
        }
        composable("login_screen") {
            LoginScreen(navController)
        }
        composable("register_screen") {
            RegisterScreen(navController)
        }
        composable("book_entry_screen") {
            BookEntryScreen(
                onBackClick = { navController.navigateUp() })
        }
    }
}
fun createShareIntent(activity: Activity, bookTitle: String) {
    val shareIntent = ShareCompat.IntentBuilder(activity)
        .setText(bookTitle)
        .setType("text/plain")
        .createChooserIntent()
        .addFlags(
            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
    activity.startActivity(shareIntent)
}