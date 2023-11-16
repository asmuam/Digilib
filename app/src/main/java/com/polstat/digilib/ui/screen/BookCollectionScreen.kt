package com.polstat.digilib.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.digilib.R

@Composable
fun BookCollectionScreen(viewModel: BookCollectionViewModel = viewModel()) {
    // Observe the bookList LiveData from the ViewModel
    val bookList by viewModel.bookList.observeAsState(emptyList())
    var keyword by remember { mutableStateOf("") }
    Log.i("booklist", "BookCollectionScreen:$bookList")

    // Update the bookList when the screen is launched
    LaunchedEffect(viewModel) {
        val dummyData = dummyData()
        viewModel.updateBookList(dummyData)
    }

    Column {
        SearchBar(onSearch = { newKeyword ->
            keyword = newKeyword
            Log.i("query", "query:$keyword")
            viewModel.filterAndUpdateBookList(keyword)
        })
        BookList(books = bookList)
    }
}

@Composable
fun BookList(books: List<Book>) { //untuk menampilkan card daftar buku
    LazyColumn {
        items(books) { book ->
            BookItem(book)
        }
    }
}

@Composable
fun BookItem(book: Book) { //card buku
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(elevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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

data class Book(val image: String, val title: String, val description: String)

fun dummyData():List<Book>{
    val bookDummies = mutableListOf<Book>()
    for (i in 1..30){
        val dummy = Book(
            image = "http://m.media-amazon.com/images/I/61ZPNhC2hSL._SY522_.jpg",
            title = "Android Programming with Kotlin for Beginners Edition $i",
            description = "Edition $i - Android is the most " +
                    "popular mobile operating system in the world and Kotlin has been " +
                    "declared by Google as a first-class programming language to build " +
                    "Android apps. With the imminent arrival of the most anticipated " +
                    "Android update, Android 10 (Q), this book gets you started " +
                    "building apps compatible with the latest version of Android."
        )
        bookDummies.add(dummy)
    }
    return bookDummies
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    var showClearIcon by rememberSaveable{
        mutableStateOf(false)
    }
    if (query.text.isEmpty()) {
        showClearIcon = false
    } else if (query.text.isNotEmpty()) {
        showClearIcon = true
    }
    TextField(
        value = query,
        onValueChange = { newQuery ->
            query = newQuery
            onSearch(query.text)
//            if (query.text.isNotEmpty()) {
//                onSearch(query.text)
//            }
        },
        leadingIcon = { Icon(imageVector = Icons.Default.Search,
            contentDescription = "") },
        placeholder = { Text("Enter keyword here...") },
        singleLine = true,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            if (showClearIcon) {
                IconButton(
                    onClick = {
                        query = TextFieldValue("")
                        onSearch("")
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


