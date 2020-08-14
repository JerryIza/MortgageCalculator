package com.example.mortgagecalculator.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mortgagecalculator.model.AppState
import com.example.mortgagecalculator.model.ExtraPayments
import com.example.mortgagecalculator.model.ScheduleOutput

@Suppress("NAME_SHADOWING")
class MainViewModel : ViewModel() {

    private val _state = MutableLiveData<AppState>()
    val state : LiveData<AppState> get() = _state

    init {
        _state.value = AppState()
    }
    //_state.value = _state.value!!.copy(checking = true)
    var stfu : ArrayList<ExtraPayments>? = null

    //make it obersable live data
    val scheduleLiveData : MutableLiveData<ArrayList<ScheduleOutput>?> = MutableLiveData()

    var scheduleArrayList: ArrayList<ScheduleOutput>? = null

        private fun getListDetails() {
            populateList()
            scheduleLiveData.value = scheduleArrayList
        }
        private fun populateList() {
            scheduleArrayList = ArrayList()
            }


        init {
            getListDetails()
        }

}
