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

package com.polstat.digilib.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.polstat.digilib.BookApplication
import com.polstat.digilib.ui.home.HomeViewModel
import com.polstat.digilib.ui.book.BookDetailsViewModel
import com.polstat.digilib.ui.book.BookEditViewModel
import com.polstat.digilib.ui.book.BookEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Book app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for BookEditViewModel
        initializer {
            BookEditViewModel(
                this.createSavedStateHandle(),
                bookApplication().container.booksRepository
            )
        }
        // Initializer for BookEntryViewModel
        initializer {
            BookEntryViewModel(bookApplication().container.booksRepository)
        }

        // Initializer for BookDetailsViewModel
        initializer {
            BookDetailsViewModel(
                this.createSavedStateHandle(),
                bookApplication().container.booksRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(bookApplication().container.booksRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [BookApplication].
 */
fun CreationExtras.bookApplication(): BookApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as BookApplication)
