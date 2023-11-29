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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.digilib.data.BooksRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update an book from the [BooksRepository]'s data source.
 */
class BookEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val booksRepository: BooksRepository
) : ViewModel() {

    /**
     * Holds current book ui state
     */
    var bookUiState by mutableStateOf(BookUiState())
        private set

    private val bookId: Int = checkNotNull(savedStateHandle[BookEditDestination.bookIdArg])

    init {
        viewModelScope.launch {
            bookUiState = booksRepository.getBookStream(bookId)
                .filterNotNull()
                .first()
                .toBookUiState(true)
        }
    }

    /**
     * Update the book in the [BooksRepository]'s data source
     */
    suspend fun updateBook() {
        if (validateInput(bookUiState.bookDetails)) {
            booksRepository.updateBook(bookUiState.bookDetails.toBook())
        }
    }

    /**
     * Updates the [bookUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(bookDetails: BookDetails) {
        bookUiState =
            BookUiState(bookDetails = bookDetails, isEntryValid = validateInput(bookDetails))
    }

    private fun validateInput(uiState: BookDetails = bookUiState.bookDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank() && quantity.isNotBlank()
        }
    }
}
