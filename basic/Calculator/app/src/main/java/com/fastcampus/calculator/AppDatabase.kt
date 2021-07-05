package com.fastcampus.calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fastcampus.calculator.dao.HistoryDao
import com.fastcampus.calculator.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
}