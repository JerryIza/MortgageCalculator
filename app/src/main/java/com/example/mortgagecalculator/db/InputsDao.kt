package com.example.mortgagecalculator.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface InputsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertInputs(inputs:Inputs)

    @Delete
     fun deleteInputs(inputs: Inputs)

}