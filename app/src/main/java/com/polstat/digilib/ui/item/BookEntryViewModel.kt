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
import androidx.lifecycle.ViewModel
import com.polstat.digilib.data.Book
import com.polstat.digilib.data.BooksRepository

/**
 * ViewModel to validate and insert books in the Room database.
 */
class BookEntryViewModel(private val booksRepository: BooksRepository) : ViewModel() {

    /**
     * Holds current book ui state
     */
    var bookUiState by mutableStateOf(BookUiState())
        private set

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
            image.isNotBlank() && title.isNotBlank() && description.isNotBlank()
        }
    }

    suspend fun saveBook() {
        if (validateInput()) {
            booksRepository.insertBook(bookUiState.bookDetails.toBook())
        }
    }

}

/**
 * Represents Ui State for an Book.
 */
data class BookUiState(
    val bookDetails: BookDetails = BookDetails(),
    val isEntryValid: Boolean = false
)

data class BookDetails(
    val id: Int = 0,
    val image: String = "",
    val title: String = "",
    val description: String = "",
    val quantity: String = "",

)

/**
 * Extension function to convert [BookDetails] to [Book]. if the value of
 * [BookDetails.quantity] is not a valid [Int], then the quantity will be set to 0
 */
fun BookDetails.toBook(): Book = Book(
    id = id,
    image = image,
    title = title,
    description = description,
    quantity = quantity.toIntOrNull() ?: 0

)



/**
 * Extension function to convert [Book] to [BookUiState]
 */
fun Book.toBookUiState(isEntryValid: Boolean = false): BookUiState = BookUiState(
    bookDetails = this.toBookDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Book] to [BookDetails]
 */
fun Book.toBookDetails(): BookDetails = BookDetails(
    id = id,
    image = image,
    title = title,
    description = description,
    quantity = quantity.toString()
)
