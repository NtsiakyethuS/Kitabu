package com.example.kitabu.data

data class BookingWithBook(
    val bookingId: Int,
    val bookOwnerId: Int,
    val userName: String,
    val bookingDate: Long,
    val returnDeadline: Long,
    val status: BookingStatus,
    val title: String,
    val author: String,
    val category: String
)
