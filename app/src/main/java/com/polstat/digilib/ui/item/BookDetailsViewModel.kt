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

package com.polstat.digilib.ui.book

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.digilib.data.BooksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete an book from the [BooksRepository]'s data source.
 */
class BookDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val booksRepository: BooksRepository,
) : ViewModel() {

    private val bookId: Int = checkNotNull(savedStateHandle[BookDetailsDestination.bookIdArg])

    /**
     * Holds the book details ui state. The data is retrieved from [BooksRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<BookDetailsUiState> =
        booksRepository.getBookStream(bookId)
            .filterNotNull()
            .map {
                BookDetailsUiState(outOfStock = it.quantity <= 0, bookDetails = it.toBookDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = BookDetailsUiState()
            )

    /**
     * Reduces the book quantity by one and update the [BooksRepository]'s data source.
     */
    fun reduceQuantityByOne() {
        viewModelScope.launch {
            val currentBook = uiState.value.bookDetails.toBook()
            if (currentBook.quantity > 0) {
                booksRepository.updateBook(currentBook.copy(quantity = currentBook.quantity - 1))
            }
        }
    }

    /**
     * Deletes the book from the [BooksRepository]'s data source.
     */
    suspend fun deleteBook() {
        booksRepository.deleteBook(uiState.value.bookDetails.toBook())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for BookDetailsScreen
 */
data class BookDetailsUiState(
    val outOfStock: Boolean = true,
    val bookDetails: BookDetails = BookDetails()
)
