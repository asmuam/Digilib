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

package com.polstat.digilib

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.polstat.digilib.data.Book
import com.polstat.digilib.data.BookDao
import com.polstat.digilib.data.BookDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BookDaoTest {

    private lateinit var bookDao: BookDao
    private lateinit var bookDatabase: BookDatabase
    private val book1 = Book(1, "Apples", "10.0", "20",6)
    private val book2 = Book(2, "Bananas", "15.0", "97",7)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        bookDatabase = Room.inMemoryDatabaseBuilder(context, BookDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        bookDao = bookDatabase.bookDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        bookDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsBookIntoDB() = runBlocking {
        addOneBookToDb()
        val allBooks = bookDao.getAllBooks().first()
        assertEquals(allBooks[0], book1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllBooks_returnsAllBooksFromDB() = runBlocking {
        addTwoBooksToDb()
        val allBooks = bookDao.getAllBooks().first()
        assertEquals(allBooks[0], book1)
        assertEquals(allBooks[1], book2)
    }


    @Test
    @Throws(Exception::class)
    fun daoGetBook_returnsBookFromDB() = runBlocking {
        addOneBookToDb()
        val book = bookDao.getBook(1)
        assertEquals(book.first(), book1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteBooks_deletesAllBooksFromDB() = runBlocking {
        addTwoBooksToDb()
        bookDao.delete(book1)
        bookDao.delete(book2)
        val allBooks = bookDao.getAllBooks().first()
        assertTrue(allBooks.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateBooks_updatesBooksInDB() = runBlocking {
        addTwoBooksToDb()
        bookDao.update(Book(1, "Apples", "15.0", "25",2))
        bookDao.update(Book(2, "Bananas", "5.0", "50",3))

        val allBooks = bookDao.getAllBooks().first()
        assertEquals(allBooks[0], Book(1, "Apples", "15.0", "25",1))
        assertEquals(allBooks[1], Book(2, "Bananas", "5.0", "50",5))
    }

    private suspend fun addOneBookToDb() {
        bookDao.insert(book1)
    }

    private suspend fun addTwoBooksToDb() {
        bookDao.insert(book1)
        bookDao.insert(book2)
    }
}
