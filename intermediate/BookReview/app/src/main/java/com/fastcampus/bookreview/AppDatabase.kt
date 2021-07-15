package com.fastcampus.bookreview

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fastcampus.bookreview.dao.HistoryDao
import com.fastcampus.bookreview.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}