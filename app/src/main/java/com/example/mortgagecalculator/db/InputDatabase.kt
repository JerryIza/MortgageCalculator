package com.example.mortgagecalculator.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Input::class],
    version = 1)

@TypeConverters(Converter::class)
abstract class InputDatabase : RoomDatabase() {

    abstract fun getInputDao(): InputDao

}