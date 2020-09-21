package com.example.mortgagecalculator.repositories

import com.example.mortgagecalculator.db.Input
import com.example.mortgagecalculator.db.InputDao
import javax.inject.Inject


class InputRepository @Inject constructor(private val inputDao: InputDao) {

    suspend fun insertInputs(input: Input) = inputDao.insertInputs(input)

    suspend fun updateInputs(input: Input) = inputDao.updateInputs(input)

    suspend fun deleteInput(input: Input) = inputDao.deleteInput(input)

    suspend fun getAllInputs() = inputDao.getAllInputs()

    fun deleteAll() = inputDao.deleteAllInputs()

    suspend fun numberOfItemsInDB() = inputDao.numberOfItemsInDB()


    //fun fetchInputs() = inputDao.fetch()




}
