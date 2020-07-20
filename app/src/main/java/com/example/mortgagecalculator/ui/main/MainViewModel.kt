package com.example.mortgagecalculator.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mortgagecalculator.model.ScheduleOutput
import kotlin.collections.ArrayList

@Suppress("NAME_SHADOWING")
class MainViewModel : ViewModel()
    {

        var scheduleLiveData : MutableLiveData<ArrayList<ScheduleOutput>?>

        var scheduleArrayList: ArrayList<ScheduleOutput>? = null


        fun getListDetails() {
            populateList()
            scheduleLiveData.value = scheduleArrayList
        }


        fun populateList() {

            val output = ScheduleOutput("", arrayListOf(),"")


            output.title = "hehehehe"
            output.something = "jaja"
            for (i in 0..2) {
                output.year.add(i.toString())
            }
            scheduleArrayList = ArrayList()
            for (i in 1..50){
                scheduleArrayList!!.add(output)
            }
        }
            //override??!?!?!?!?
            // initialize with out on click listener.
        init {
            scheduleLiveData = MutableLiveData()

            getListDetails()
        }

}
