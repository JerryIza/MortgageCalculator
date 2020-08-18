package com.example.mortgagecalculator.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mortgagecalculator.model.AmortizationCalculator
import com.example.mortgagecalculator.model.AmortizationResults
import com.example.mortgagecalculator.model.AppState

@Suppress("NAME_SHADOWING")
class AmortizationViewModel : ViewModel() {

    private val _state = MutableLiveData<AppState>()
    val state: LiveData<AppState> get() = _state

    init {
        _state.value = AppState()
    }
    //_state.value = _state.value!!.copy(checking = true)

    val scheduleLiveData: MutableLiveData<ArrayList<AmortizationResults>?> = MutableLiveData()

    var scheduleArrayList: ArrayList<AmortizationResults>? = null


    fun calculateTest(amortizationCalculator: AmortizationCalculator) {
        println("4")

        println("Array list OLD: "+ scheduleArrayList)


        val calculatorResults = (amortizationCalculator.getAmortization(
            state.value!!.loanAmount,
            state.value!!.yearAmount,
            state.value!!.interestAmount,
            state.value!!.downAmount
        ))
        this.scheduleArrayList?.clear()
        for (i in 0 until (state.value!!.yearAmount * 12)) {
            scheduleArrayList?.add(
                AmortizationResults(
                    calculatorResults!![i].quotas,
                    calculatorResults[i].loanLeft,
                    calculatorResults[i].interest,
                    calculatorResults[i].principal,
                    calculatorResults[i].totalInterest
                )
            )
            scheduleArrayList?.get(0)!!.totalInterest
            //println("5")


        }
        println("Array list results NEW: "+ scheduleArrayList)
    }

    private fun getListDetails() {
        populateList()
        scheduleLiveData.value = scheduleArrayList
    }

    private fun populateList() {

        scheduleArrayList = ArrayList()
        scheduleArrayList?.add(
            AmortizationResults(
                "", "", "", "", ""
            )
        )
    }

    init {
        println("6")

        getListDetails()
    }

}


