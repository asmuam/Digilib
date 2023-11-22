package com.polstat.digilib.ui.screen

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
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
        if (_bookList.value.isNullOrEmpty()) {
            _bookList.value = newBookList // Inisialisasi _bookList jika kosong
        }
    }

    // Fungsi untuk memfilter dan memperbarui daftar buku berdasarkan kata kunci
    fun filterBooks(keyword: String) {
        Log.d("BookCollectionViewModel", "Filtering books with keyword: $keyword")

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

    private val _query = MutableLiveData(TextFieldValue(""))
    val query: LiveData<TextFieldValue> get() = _query

    // Fungsi untuk mengubah nilai query
    fun updateQuery(newQuery: TextFieldValue) {
        _query.value = newQuery
    }

    // Fungsi untuk mendapatkan detail buku berdasarkan ID
    fun getBookById(bookId: Int?): Book? {
        Log.i("ID GET", "BookDetailScreen: ${bookId}")
        Log.i("BOOKLIST", "BookDetailScreen: ${_originalBookList.value}")
        val validBookId = bookId ?: return null // Jika bookId null, kembalikan null

        // Ubah logika ini sesuai dengan cara Anda mendapatkan detail buku dari ID
        return _originalBookList.value?.firstOrNull { it.id == validBookId }
    }

}
