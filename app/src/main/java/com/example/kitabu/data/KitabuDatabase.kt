package com.example.kitabu.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [BookEntity::class, BookingEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class KitabuDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile private var INSTANCE: KitabuDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): KitabuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KitabuDatabase::class.java,
                    "kitabu_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(KitabuDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class KitabuDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    prePopulate(database.bookDao())
                }
            }
        }

        suspend fun prePopulate(bookDao: BookDao) {
            // Add sample books on first run
            bookDao.insertBook(BookEntity(title = "1984", author = "George Orwell", category = "Fiction"))
            bookDao.insertBook(BookEntity(title = "Clean Code", author = "Robert C. Martin", category = "Programming"))
            bookDao.insertBook(BookEntity(title = "The Alchemist", author = "Paulo Coelho", category = "Philosophy"))
        }
    }
}
