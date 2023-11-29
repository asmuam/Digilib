/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.polstat.digilib.ui.navigation

import LoginScreen
import LoginScreenDestination
import RegisterScreen
import RegisterScreenDestination
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.polstat.digilib.ui.home.HomeDestination
import com.polstat.digilib.ui.home.HomeScreen
import com.polstat.digilib.ui.book.BookDetailsDestination
import com.polstat.digilib.ui.book.BookDetailsScreen
import com.polstat.digilib.ui.book.BookEditDestination
import com.polstat.digilib.ui.book.BookEditScreen
import com.polstat.digilib.ui.book.BookEntryDestination
import com.polstat.digilib.ui.book.BookEntryScreen
import com.polstat.digilib.ui.screen.WelcomeScreen
import com.polstat.digilib.ui.screen.WelcomeScreenDestination

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun BookNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = WelcomeScreenDestination.route,
        modifier = modifier
    ) {
        composable(WelcomeScreenDestination.route) {
            WelcomeScreen(navController)
        }
        composable(LoginScreenDestination.route) {
            LoginScreen(navController)
        }
        composable(RegisterScreenDestination.route) {
            RegisterScreen(navController)
        }
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToBookEntry = { navController.navigate(BookEntryDestination.route) },
                navigateToBookUpdate = {
                    navController.navigate("${BookDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = BookEntryDestination.route) {
            BookEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = BookDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(BookDetailsDestination.bookIdArg) {
                type = NavType.IntType
            })
        ) {
            BookDetailsScreen(
                navigateToEditBook = { navController.navigate("${BookEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = BookEditDestination.routeWithArgs,
            arguments = listOf(navArgument(BookEditDestination.bookIdArg) {
                type = NavType.IntType
            })
        ) {
            BookEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
