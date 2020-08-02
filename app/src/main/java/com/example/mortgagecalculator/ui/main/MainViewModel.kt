package com.example.mortgagecalculator.ui.main

import android.provider.SyncStateContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mortgagecalculator.model.ScheduleOutput
import kotlin.collections.ArrayList

@Suppress("NAME_SHADOWING")
class MainViewModel : ViewModel() {

    val scheduleLiveData : MutableLiveData<ArrayList<ScheduleOutput>?>

        var scheduleArrayList: ArrayList<ScheduleOutput>? = null

        private fun getListDetails() {
            populateList()
            scheduleLiveData.value = scheduleArrayList
        }


        private fun populateList() {
            val output = ScheduleOutput("YEAR", "SOMETHING")
            scheduleArrayList = ArrayList()
            scheduleArrayList!!.add(output)
            }


        init {
            scheduleLiveData = MutableLiveData()
            getListDetails()
        }

}
