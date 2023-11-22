package com.polstat.digilib.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.polstat.digilib.R

@Composable
fun BookDetailScreen(
    book: Book,
    onBackClick: () -> Unit,
    onShareClick: (String) -> Unit,
) {

    Column {
        Box {
            Image(
                painter = painterResource(id = R.drawable.book1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(
            text = book.title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = book.description,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis
        )
        Row {
            Button(onClick = { onBackClick() }) {
                Text(text = "Back")
            }
            Button(onClick = { onShareClick(book.title) }) {
                Text(text = "Share")
            }
        }
    }
}