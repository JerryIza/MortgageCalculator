package com.example.mortgagecalculator.repositories

import androidx.lifecycle.LiveData
import com.example.mortgagecalculator.model.AmortizationCalculator
import com.example.mortgagecalculator.model.AmortizationResults
import com.example.mortgagecalculator.model.AppState
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val calculator: AmortizationCalculator,
) {



    fun getSchedule(appState: LiveData<AppState>, extraPayments: MutableMap<String, Int>): ArrayList<AmortizationResults>? = calculator.getAmortization(
        appState.value!!.loanAmount,
        appState.value!!.yearAmount,
        appState.value!!.interestAmount,
        appState.value!!.downAmount,
        extraPayments
    )


}


/*private var savedExtraPayments = mutableMapOf<String, ExtraPayments>()

fun loadExtraPaymentsByMonth(monthNumber: String): ExtraPayments? {
    return savedExtraPayments[monthNumber]
}

fun loadExtraPaymentsAmounts(): LiveData<List<ExtraPayments>> {
    val liveData = MutableLiveData<List<ExtraPayments>>()
    liveData.value = savedExtraPayments.values.toList()
    return liveData
}*/