package com.example.kitabu.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    // CREATE
    @Insert suspend fun insertBook(book: BookEntity)

    // READ
    @Query("SELECT * FROM BookEntity WHERE isAvailable = 1")
    fun getAvailableBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM BookEntity WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM BookEntity")
    fun getAllBooks(): Flow<List<BookEntity>>

    // UPDATE
    @Update suspend fun updateBook(book: BookEntity)

    // DELETE
    @Delete suspend fun deleteBook(book: BookEntity)
}
