package com.example.kitabu.data

class LibraryRepository {
}class LibraryRepository(
    private val bookDao: BookDao,
    private val bookingDao: BookingDao
) {
    fun getAvailableBooks() = bookDao.getAvailableBooks()
    fun searchBooks(query: String) = bookDao.searchBooks(query)
    fun getActiveBookings() = bookingDao.getActiveBookingsWithBookInfo()

    suspend fun reserveBook(book: BookEntity, booking: BookingEntity) {
        bookDao.updateBook(book.copy(isAvailable = false))
        bookingDao.insertBooking(booking)
    }

    suspend fun renewBooking(bookingId: Int, newDeadline: Long) {
        bookingDao.extendRentalPeriod(bookingId, newDeadline)
    }

    suspend fun returnBook(booking: BookingEntity, book: BookEntity) {
        bookingDao.updateBooking(booking.copy(status = BookingStatus.RETURNED))
        bookDao.updateBook(book.copy(isAvailable = true))
    }

    suspend fun cancelBooking(booking: BookingEntity) {
        bookingDao.deleteBooking(booking)
    }
}
