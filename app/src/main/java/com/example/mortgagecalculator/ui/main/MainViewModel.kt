package com.example.mortgagecalculator.ui.main

import android.provider.SyncStateContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mortgagecalculator.model.MortgageDefaults
import com.example.mortgagecalculator.model.ScheduleOutput
import kotlin.collections.ArrayList

@Suppress("NAME_SHADOWING")
class MainViewModel : ViewModel() {

    private val _state = MutableLiveData<MortgageDefaults>()
    val state : LiveData<MortgageDefaults> get() = _state

    init {
        _state.value = MortgageDefaults()
    }
    //_state.value = _state.value!!.copy(checking = true)


    val scheduleLiveData : MutableLiveData<ArrayList<ScheduleOutput>?>

        var scheduleArrayList: ArrayList<ScheduleOutput>? = null

        private fun getListDetails() {
            populateList()
            scheduleLiveData.value = scheduleArrayList
        }


        private fun populateList() {
            val output = ScheduleOutput("YEAR", "SOMETHING","INTEREST", "PRINCIPAL")
            scheduleArrayList = ArrayList()
            scheduleArrayList!!.add(output)
            }


        init {
            scheduleLiveData = MutableLiveData()
            getListDetails()
        }

}
