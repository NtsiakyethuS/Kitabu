package com.example.kitabu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kitabu.data.BookEntity
import com.example.kitabu.data.BookingEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReservationsDashboard(
    bookings: List<Pair<BookingEntity, BookEntity>> = sampleBookings,
    onRenew: (BookingEntity) -> Unit = {},
    onReturn: (BookingEntity) -> Unit = {},
    onCancel: (BookingEntity) -> Unit = {},
    onBackToCatalog: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "User Reservations Dashboard",
                style = MaterialTheme.typography.titleLarge,
            )
            OutlinedButton(onClick = onBackToCatalog) {
                Text("Back to Catalog")
            }
        }

        if (bookings.isEmpty()) {
            Text(
                text = "No active reservations found.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp),
            )
        } else {
            LazyColumn {
                items(bookings) { (booking, book) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Title: ${book.title}", style = MaterialTheme.typography.titleMedium)
                            Text("Author: ${book.author}", style = MaterialTheme.typography.bodyMedium)
                            Text("Category: ${book.category}", style = MaterialTheme.typography.bodySmall)

                            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            Text("Reserved: ${dateFormat.format(Date(booking.bookingDate))}")
                            Text("Deadline: ${dateFormat.format(Date(booking.returnDeadline))}")

                            val daysRemaining =
                                ((booking.returnDeadline - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()
                            Text("Days remaining: $daysRemaining")

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { onRenew(booking) }) { Text("Renew") }
                                Button(onClick = { onReturn(booking) }) { Text("Return") }
                                Button(onClick = { onCancel(booking) }) { Text("Cancel") }
                            }
                        }
                    }
                }
            }
        }
    }
}

private val sampleBookings = listOf(
    Pair(
        BookingEntity(
            id = "b1",
            bookId = "1",
            bookingDate = System.currentTimeMillis(),
            returnDeadline = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L),
        ),
        BookEntity(
            id = "1",
            title = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            category = "Fiction",
            isAvailable = false,
        ),
    ),
)
