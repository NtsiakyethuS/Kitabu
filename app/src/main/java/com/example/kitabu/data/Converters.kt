package com.example.kitabu.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStatus(value: BookingStatus): String = value.name

    @TypeConverter
    fun toStatus(value: String): BookingStatus = BookingStatus.valueOf(value)

    @TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: java.util.Date?): Long? {
        return date?.time
    }
}
