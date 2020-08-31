package com.example.mortgagecalculator.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface InputsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInputs(inputs:Inputs)

    @Delete
    suspend fun deleteInputs(inputs: Inputs)

}