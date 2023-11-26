package com.polstat.digilib.ui.screen

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.polstat.digilib.R


@Composable
@Preview
fun BookCollectionScreenPreview() {
    val bookCollectionViewModel: BookCollectionViewModel = viewModel()
    val dummy = dummyData()
    val navController = rememberNavController()
    bookCollectionViewModel.updateBookList(dummy)
    BookCollectionScreen(
        viewModel = bookCollectionViewModel, // Sesuaikan dengan inisialisasi ViewModel
        onBookClick = { /* handle klik buku */ },
        Modifier,
        navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCollectionScreen(
    viewModel: BookCollectionViewModel = viewModel(),
    onBookClick: (Book) -> Unit,
    modifier: Modifier,
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text("Book Collection") // Ganti dengan judul yang sesuai
                },
                navigationIcon = {
                    // Tambahkan ikon navigasi jika diperlukan
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("book_entry_screen")
                },
                shape = MaterialTheme.shapes.medium,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Icon",
                    tint = Color.White // Ganti dengan warna yang diinginkan
                )
            }
        },
        content = { innerPadding ->
            BookCollectionScreenBody(
                viewModel,
                onBookClick,
                modifier = Modifier
                        .padding(innerPadding)
            )
        }
    )
}
@Composable
fun BookCollectionScreenBody(
    viewModel: BookCollectionViewModel = viewModel(),
    onBookClick: (Book) -> Unit,
    modifier: Modifier
){
    // Observe the bookList LiveData from the ViewModel
    val books by viewModel.bookList.observeAsState(emptyList())
    var bookList by rememberSaveable { mutableStateOf(emptyList<Book>()) }

    // Update the bookList when the screen is launched
    LaunchedEffect(viewModel) {
        val dummyData = dummyData()
        viewModel.updateBookList(dummyData)
    }

    // Observe changes in the bookList and update the local state
    bookList = books

    Column {
        SearchBar(viewModel=viewModel,onSearch = { newKeyword ->
            viewModel.filterBooks(newKeyword)
        })
        BookList(books = bookList, onBookClick=onBookClick)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(viewModel: BookCollectionViewModel, onSearch: (String) -> Unit) {
    val query by viewModel.query.observeAsState(TextFieldValue(""))
    var showClearIcon by rememberSaveable{mutableStateOf(query.text.isNotEmpty())}

    TextField(
        value = query,
        onValueChange = {
            viewModel.updateQuery(it)
            onSearch(it.text)
            showClearIcon = it.text.isNotEmpty()
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
fun BookList(books: List<Book>, onBookClick: (Book) -> Unit) { //untuk menampilkan card daftar buku
    LazyColumn {
        items(books) { book ->
            BookItem(book = book, onBookClick = onBookClick)        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(book: Book, onBookClick: (Book) -> Unit) { //card buku
    Card(
        onClick = {onBookClick(book)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(elevation = 4.dp)

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

data class Book(val id: Int, val image: String, val title: String, val description: String)

fun dummyData(): List<Book> {
    val bookDummies = mutableListOf<Book>()
    for (i in 1..30) {
        val dummy = Book(
            id = i-1,
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


