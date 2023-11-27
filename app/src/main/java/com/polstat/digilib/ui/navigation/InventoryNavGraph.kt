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
import com.polstat.digilib.ui.item.ItemDetailsDestination
import com.polstat.digilib.ui.item.ItemDetailsScreen
import com.polstat.digilib.ui.item.ItemEditDestination
import com.polstat.digilib.ui.item.ItemEditScreen
import com.polstat.digilib.ui.item.ItemEntryDestination
import com.polstat.digilib.ui.item.ItemEntryScreen
import com.polstat.digilib.ui.screen.WelcomeScreen
import com.polstat.digilib.ui.screen.WelcomeScreenDestination

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun InventoryNavHost(
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
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
