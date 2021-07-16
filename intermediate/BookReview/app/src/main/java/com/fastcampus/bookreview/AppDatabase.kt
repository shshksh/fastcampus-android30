package com.fastcampus.bookreview

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fastcampus.bookreview.dao.HistoryDao
import com.fastcampus.bookreview.dao.ReviewDao
import com.fastcampus.bookreview.model.History
import com.fastcampus.bookreview.model.Review

@Database(entities = [History::class, Review::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    abstract fun reviewDao(): ReviewDao
}