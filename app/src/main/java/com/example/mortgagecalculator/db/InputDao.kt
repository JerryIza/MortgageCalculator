package com.example.mortgagecalculator.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface InputDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInputs(input: Input)

    @Update
    suspend fun updateInputs(input: Input)

    @Delete
    suspend fun deleteInput(input: Input)

    @Query( "DELETE FROM inputs_table")
    fun deleteAllInputs()

    @Query("SELECT count(id) FROM inputs_table")
    suspend fun numberOfItemsInDB() : Int

    @Query("SELECT * from inputs_table")
    fun getAllInputs() : LiveData<List<Input>>



}