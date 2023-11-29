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

package com.polstat.digilib.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Book] from a given data source.
 */
interface BooksRepository {
    /**
     * Retrieve all the books from the the given data source.
     */
    fun getAllBooksStream(): Flow<List<Book>>

    /**
     * Retrieve an book from the given data source that matches with the [id].
     */
    fun getBookStream(id: Int): Flow<Book?>

    /**
     * Insert book in the data source
     */
    suspend fun insertBook(book: Book)

    /**
     * Delete book from the data source
     */
    suspend fun deleteBook(book: Book)

    /**
     * Update book in the data source
     */
    suspend fun updateBook(book: Book)
}
