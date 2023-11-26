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

package com.polstat.digilib.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.digilib.R
import com.polstat.digilib.navigation.NavigationDestination


object ItemEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = R.string.edit_item_title
    const val itemIdArg = "itemId"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookEditScreen(
    book: Book,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookEditViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Book") // Ganti dengan judul yang sesuai
                },
                navigationIcon = {
                    // Tambahkan ikon navigasi jika diperlukan
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        ItemEntryBody(
            itemUiState = viewModel.itemUiState,
            onItemValueChange = { },
            onSaveClick = { },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemEditScreenPreview() {
        BookEditScreen(
            book = Book(
                    id = 1,
                    image = "http://m.media-amazon.com/images/I/61ZPNhC2hSL._SY522_.jpg",
                    title = "Android Programming with Kotlin for Beginners Edition 1",
                    description = "Edition 1 - Android is the most " +
                            "popular mobile operating system in the world and Kotlin has been " +
                            "declared by Google as a first-class programming language to build " +
                            "Android apps. With the imminent arrival of the most anticipated " +
                            "Android update, Android 10 (Q), this book gets you started " +
                            "building apps compatible with the latest version of Android."
                ),
            onBackClick = { /*Do nothing*/ })
}
