package com.example.kitabu.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    // CREATE
    @Insert suspend fun insertBooking(booking: BookingEntity)

    // READ
    @Query("""
        SELECT BookingEntity.*, BookEntity.title, BookEntity.author, BookEntity.category 
        FROM BookingEntity 
        INNER JOIN BookEntity ON BookingEntity.bookOwnerId = BookEntity.bookId
        WHERE BookingEntity.status = 'ACTIVE'
    """)
    fun getActiveBookingsWithBookInfo(): Flow<List<BookingWithBook>>

    @Query("SELECT * FROM BookingEntity WHERE status = 'PENDING'")
    fun getPendingBookings(): Flow<List<BookingEntity>>

    // UPDATE
    @Query("UPDATE BookingEntity SET returnDeadline = :newDeadline WHERE bookingId = :bookingId")
    suspend fun extendRentalPeriod(bookingId: Int, newDeadline: Long)

    @Transaction
    suspend fun processReturn(booking: BookingEntity, bookDao: BookDao) {
        // Update booking status
        updateBooking(booking.copy(status = BookingStatus.RETURNED))
        // Free up book availability
        val book = bookDao.getBookById(booking.bookOwnerId)
        bookDao.updateBook(book.copy(isAvailable = true))
    }

    @Update suspend fun updateBooking(booking: BookingEntity)

    // DELETE
    @Delete suspend fun deleteBooking(booking: BookingEntity)
}
