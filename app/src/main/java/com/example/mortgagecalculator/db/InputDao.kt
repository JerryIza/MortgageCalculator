package com.example.mortgagecalculator.db

import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class InputDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertInputs(input: Input)

    @Update
    abstract suspend fun updateInputs(input: Input)

    @Delete
    abstract suspend fun deleteInput(input: Input)

    @Query( "DELETE FROM inputs_table")
    abstract fun deleteAllInputs()

    @Query("SELECT * from inputs_table ORDER BY modified_at desc")
    abstract suspend fun getAllInputs() : List<Input>

    @Query("SELECT count(savedInputsTitle) FROM inputs_table")
    abstract suspend fun numberOfItemsInDB() : Int


}