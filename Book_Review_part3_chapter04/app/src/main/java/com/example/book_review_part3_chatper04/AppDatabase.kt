package com.example.book_review_part3_chatper04

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.book_review_part3_chatper04.dao.HistoryDao
import com.example.book_review_part3_chatper04.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}