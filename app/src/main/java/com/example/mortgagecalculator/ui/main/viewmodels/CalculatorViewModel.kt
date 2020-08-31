package com.example.mortgagecalculator.ui.main.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mortgagecalculator.model.AppState

import com.example.mortgagecalculator.repositories.MainRepository

class CalculatorViewModel @ViewModelInject constructor(
    private var mainRepository: MainRepository,
    private var appState: AppState
): ViewModel(){


    private val _state = MutableLiveData<AppState>()

    val state: LiveData<AppState> get() = _state

    val extraPaymentsMap = mutableMapOf<String, Int>()

    val extraPaymentsLiveData: MutableMap<String, Int> get() = extraPaymentsMap


    fun getResults() = mainRepository.getSchedule(state, extraPaymentsLiveData)



    init {
        _state.value = AppState()
    }



}