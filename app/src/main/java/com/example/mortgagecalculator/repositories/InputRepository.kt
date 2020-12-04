package com.example.mortgagecalculator.repositories

import com.example.mortgagecalculator.db.Input
import com.example.mortgagecalculator.db.InputDao
import javax.inject.Inject


class InputRepository @Inject constructor(private val inputDao: InputDao) {

    suspend fun insertInputs(input: Input) = inputDao.insertInputs(input)

    suspend fun deleteInput(input: Input) = inputDao.deleteInput(input)

    suspend fun getAllInputs() = inputDao.getAllInputs()

    suspend fun numberOfItemsInDB() = inputDao.numberOfItemsInDB()

}
