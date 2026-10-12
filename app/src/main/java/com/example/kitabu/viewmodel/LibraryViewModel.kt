package com.example.kitabu.viewmodel

import androidx.lifecycle.ViewModel
import com.example.kitabu.data.BookEntity
import com.example.kitabu.data.BookingEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LibraryViewModel : ViewModel() {

    private val _books = MutableStateFlow(initialBooks)
    val books: StateFlow<List<BookEntity>> = _books.asStateFlow()

    private val _bookings = MutableStateFlow<List<Pair<BookingEntity, BookEntity>>>(emptyList())
    val bookings: StateFlow<List<Pair<BookingEntity, BookEntity>>> = _bookings.asStateFlow()

    fun reserveBook(book: BookEntity) {
        val currentBooks = _books.value
        val bookToReserve = currentBooks.find { it.id == book.id } ?: return
        if (!bookToReserve.isAvailable) return

        val newBooking = BookingEntity(
            id = "b_${System.currentTimeMillis()}",
            bookId = book.id,
            bookingDate = System.currentTimeMillis(),
            returnDeadline = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L),
        )

        val updatedBook = bookToReserve.copy(isAvailable = false)

        _books.update { list ->
            list.map { if (it.id == book.id) updatedBook else it }
        }

        _bookings.update { list ->
            list + Pair(newBooking, updatedBook)
        }
    }

    fun renewBooking(booking: BookingEntity) {
        _bookings.update { list ->
            list.map { (b, book) ->
                if (b.id == booking.id) {
                    val newDeadline = b.returnDeadline + (7 * 24 * 60 * 60 * 1000L)
                    Pair(b.copy(returnDeadline = newDeadline), book)
                } else {
                    Pair(b, book)
                }
            }
        }
    }

    fun returnBook(booking: BookingEntity) {
        _bookings.update { list ->
            list.filter { (b, _) -> b.id != booking.id }
        }

        _books.update { list ->
            list.map { if (it.id == booking.bookId) it.copy(isAvailable = true) else it }
        }
    }

    fun cancelBooking(booking: BookingEntity) {
        returnBook(booking)
    }
}

private val initialBooks = listOf(
    BookEntity(id = "1", title = "The Great Gatsby", author = "F. Scott Fitzgerald", category = "Fiction", isAvailable = true),
    BookEntity(id = "2", title = "1984", author = "George Orwell", category = "Dystopian", isAvailable = true),
    BookEntity(id = "3", title = "To Kill a Mockingbird", author = "Harper Lee", category = "Classic", isAvailable = true),
    BookEntity(id = "4", title = "Pride and Prejudice", author = "Jane Austen", category = "Romance", isAvailable = true),
)
