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

package com.polstat.digilib.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.digilib.BookTopAppBar
import com.polstat.digilib.R
import com.polstat.digilib.data.AppDataContainer
import com.polstat.digilib.data.Book
import com.polstat.digilib.ui.AppViewModelProvider
import com.polstat.digilib.ui.navigation.NavigationDestination
import com.polstat.digilib.ui.theme.BookTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToBookEntry: () -> Unit,
    navigateToBookUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BookTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToBookEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.book_entry_title)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            bookList = homeUiState.bookList,
            onBookClick = navigateToBookUpdate,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            viewModel
        )
    }
}

@Composable
private fun HomeBody(
    bookList: List<Book>,
    onBookClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel // Tambahkan parameter viewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        SearchBar(viewModel=viewModel,onSearch = { newKeyword ->
            viewModel.updateQuery(TextFieldValue(newKeyword))
        })
        // Check if there is an active search query
        val queryState = viewModel.query.collectAsState()
        val isQueryActive = queryState.value.text.isNotEmpty()

        if (bookList.isEmpty() && isQueryActive) {

            Text(
                text = stringResource(R.string.no_book_found_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else if (bookList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_book_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            BookList(
                bookList = bookList,
                onBookClick = { onBookClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun SearchBar(viewModel: HomeViewModel, onSearch: (String) -> Unit) {
    val query by viewModel.query.collectAsState()
    var showClearIcon by rememberSaveable { mutableStateOf(query.text.isNotEmpty()) }
    TextField(
        value = query.text,
        onValueChange = {
            onSearch(it)
            showClearIcon = it.isNotEmpty()
        },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
        placeholder = { Text("Enter keyword here...") },
        singleLine = true,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            if (showClearIcon) {
                IconButton(
                    onClick = {
                        viewModel.updateQuery(TextFieldValue(""))
                        onSearch("")
                        showClearIcon = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "Clear Icon"
                    )
                }
            }
        }
    )
}
@Composable
private fun BookList(
    bookList: List<Book>, onBookClick: (Book) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = bookList, key = { it.id }) { book ->
            BookItem(book = book,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onBookClick(book) })
        }
    }
}

@Composable
private fun BookItem(
    book: Book, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium) // Clip the Box with a shape
        ) {
            Image(
                painter = painterResource(id = R.drawable.book1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface) // Add a background color
        ) {
            Text(
                text = book.title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = book.description,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
        }
    }


@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    BookTheme {
        val viewModel = HomeViewModel(AppDataContainer(LocalContext.current).booksRepository)
        HomeBody(listOf(
            Book(1, "Game", "100.0", "20",4), Book(2, "Pen", "200.0", "30", quantity = 2), Book(3, "TV", "300.0", "50", quantity = 7)
        ), onBookClick = {}, viewModel = viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    BookTheme {
        val viewModel = HomeViewModel(AppDataContainer(LocalContext.current).booksRepository)
        HomeBody(listOf(), onBookClick = {}, viewModel = viewModel
        )
    }
}
