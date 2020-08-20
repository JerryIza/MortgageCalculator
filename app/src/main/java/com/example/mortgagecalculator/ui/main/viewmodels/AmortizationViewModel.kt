package com.example.mortgagecalculator.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mortgagecalculator.model.AmortizationCalculator
import com.example.mortgagecalculator.model.AmortizationResults
import com.example.mortgagecalculator.model.AppState
import com.example.mortgagecalculator.model.ExtraPayments

@Suppress("NAME_SHADOWING")
class AmortizationViewModel : ViewModel() {

    private val _state = MutableLiveData<AppState>()

    val state: LiveData<AppState> get() = _state

    val extraPaymentsMap = mutableMapOf<String, Int>()

    val extraPaymentsLiveData: MutableMap<String, Int> get() = extraPaymentsMap

    //private?
    var scheduleArrayList: ArrayList<AmortizationResults>? = null

    val scheduleLiveData: MutableLiveData<ArrayList<AmortizationResults>?> = MutableLiveData()


    fun calculateTest(amortizationCalculator: AmortizationCalculator) {

        scheduleArrayList?.clear()

        val calculatorResults = (amortizationCalculator.getAmortization(
            state.value!!.loanAmount,
            state.value!!.yearAmount,
            state.value!!.interestAmount,
            state.value!!.downAmount,
            extraPaymentsLiveData
            ))
        println(scheduleArrayList)
        for (i in 0 until (calculatorResults!!.last().quotas.toInt())) {
            scheduleArrayList?.add(
                AmortizationResults(
                    calculatorResults[i].quotas,
                    calculatorResults[i].loanLeft,
                    calculatorResults[i].interest,
                    calculatorResults[i].principal,
                    calculatorResults[i].totalInterest,
                   additionalPayment =  calculatorResults[i].additionalPayment
                )
            )
        }
        scheduleArrayList?.last()?.totalAmount = calculatorResults[0].totalAmount
        scheduleArrayList?.last()?.monthlyPayment = calculatorResults[0].monthlyPayment
    }

    private fun getListDetails() {
        populateScheduleList()
        //starting text views
        scheduleLiveData.value = scheduleArrayList
    }

    private fun populateScheduleList() {
        scheduleArrayList = ArrayList()
    }

    init {
        getListDetails()
        _state.value = AppState()
    }

}


