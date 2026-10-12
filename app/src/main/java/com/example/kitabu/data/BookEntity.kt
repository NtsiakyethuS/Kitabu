package com.example.kitabu.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class BookEntity(

    @PrimaryKey(autoGenerate = true) val bookId: Int = 0,
    val title: String,
    val author: String,
    val category: String,
    val isAvailable: Boolean = true,
)
