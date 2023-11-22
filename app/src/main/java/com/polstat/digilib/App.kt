package com.polstat.digilib

import LoginScreen
import RegisterScreen
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
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
import com.polstat.digilib.ui.screen.WelcomeScreen
import com.polstat.digilib.ui.screen.dummyData

@Composable
fun App() {
    val navController = rememberNavController()
    val activity = (LocalContext.current as Activity)
    NavHost(
        navController = navController,
        startDestination = "welcome_screen"
    ) {
        composable("welcome_screen") {
            WelcomeScreen(navController)
        }
        composable("book_collection_screen") {
            BookCollectionScreen(
                onBookClick = {
                    Log.i("ID BOOK", "BookDetailScreen: ${it.id}")
                    Log.i("ID BOOK", "BookDetailScreen: ${it}")
                    navController.navigate("books/${it.id}")
                })
        }
        composable(
            "books/{bookid}",
            arguments = listOf(navArgument("bookid") {
                type = NavType.IntType
            })
        ) {
            val id = it.arguments?.getInt("bookid")
            Log.i("ID", "BookDetailScreen: ${id}")

            val viewModel: BookCollectionViewModel = viewModel()
            val dummyData = dummyData()
            viewModel.updateBookList(dummyData)
            // Mendapatkan detail buku berdasarkan ID
            val book = viewModel.getBookById(id)
            Log.i("BOOK", "BookDetailScreen: ${book}")

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
    }
}

fun createShareIntent(activity: Activity, bookTitle: String) {
    val shareText = bookTitle
    val shareIntent = ShareCompat.IntentBuilder(activity)
        .setText(shareText)
        .setType("text/plain")
        .createChooserIntent()
        .addFlags(
            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
    activity.startActivity(shareIntent)
}