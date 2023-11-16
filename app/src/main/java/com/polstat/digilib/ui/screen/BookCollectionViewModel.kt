package com.polstat.digilib.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookCollectionViewModel : ViewModel() {
    private val _originalBookList = MutableLiveData<List<Book>>() // Simpan daftar buku asli
    private val _bookList = MutableLiveData<List<Book>>()
    val bookList: LiveData<List<Book>> get() = _bookList

    // Fungsi untuk memperbarui data buku
    fun updateBookList(newBookList: List<Book>) {
        _originalBookList.value = newBookList // Simpan daftar buku asli
        _bookList.value = newBookList
    }

    // Fungsi untuk memfilter dan memperbarui daftar buku berdasarkan kata kunci
    fun filterAndUpdateBookList(keyword: String) {
        val originalList = _originalBookList.value ?: emptyList()

        val filteredList = if (keyword.isNotEmpty()) {
            originalList.filter { book ->
                book.title.lowercase().contains(keyword.lowercase())
            }
        } else {
            originalList
        }

        _bookList.value = filteredList
    }
}
