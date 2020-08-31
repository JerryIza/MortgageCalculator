package com.example.mortgagecalculator.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Inputs::class],
    version = 1
)

abstract class InputsDatabase : RoomDatabase(){

    abstract fun getInputsDao():InputsDao
}