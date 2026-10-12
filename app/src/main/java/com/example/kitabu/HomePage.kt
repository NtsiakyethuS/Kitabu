package com.example.kitabu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kitabu.data.BookEntity

@Composable
fun HomePage(
    books: List<BookEntity> = sampleBooks,
    onReserveClick: (BookEntity) -> Unit = {},
) {
    BookCatalogScreen(books = books, onReserveClick = onReserveClick)
}

private val sampleBooks = listOf(
    BookEntity(id = "1", title = "The Great Gatsby", author = "F. Scott Fitzgerald", category = "Fiction", isAvailable = true),
    BookEntity(id = "2", title = "1984", author = "George Orwell", category = "Dystopian", isAvailable = false),
    BookEntity(id = "3", title = "To Kill a Mockingbird", author = "Harper Lee", category = "Classic", isAvailable = true),
    BookEntity(id = "4", title = "Pride and Prejudice", author = "Jane Austen", category = "Romance", isAvailable = true),
)

@Composable
fun BookCatalogScreen(
    books: List<BookEntity>,
    onReserveClick: (BookEntity) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedBook by remember { mutableStateOf<BookEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Top Row with Search Box
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search books") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Text(
            text = "Book Catalog",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        // Filter books by search query
        val filteredBooks = books.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.author.contains(searchQuery, ignoreCase = true)
        }

        LazyColumn {
            items(filteredBooks, key = { it.id }) { book ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(book.title, style = MaterialTheme.typography.titleMedium)
                        Text(book.author, style = MaterialTheme.typography.bodyMedium)
                        Text(book.category, style = MaterialTheme.typography.bodySmall)

                        val badgeColor = if (book.isAvailable) Color.Unspecified else Color.Red
                        Text(
                            if (book.isAvailable) "Available" else "Borrowed",
                            color = badgeColor,
                            style = MaterialTheme.typography.bodySmall,
                        )

                        if (book.isAvailable) {
                            Button(
                                onClick = { selectedBook = book },
                                modifier = Modifier.padding(top = 8.dp),
                            ) {
                                Text("Reserve")
                            }
                        }
                    }
                }
            }
        }
    }

    // Reservation Dialog
    selectedBook?.let { book ->
        AlertDialog(
            onDismissRequest = { selectedBook = null },
            title = { Text("Reserve ${book.title}") },
            text = {
                Column {
                    Text("Select rental duration (days):")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReserveClick(book)
                        selectedBook = null
                    },
                ) {
                    Text("Reserve")
                }
            },
            dismissButton = {
                Button(
                    onClick = { selectedBook = null },
                ) {
                    Text("Cancel")
                }
            },
        )
    }
}
