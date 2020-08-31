package com.example.mortgagecalculator.ui.main.viewmodels

import android.icu.text.SimpleDateFormat
import android.net.ParseException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mortgagecalculator.model.AmortizationCalculator
import com.example.mortgagecalculator.model.AmortizationResults
import com.example.mortgagecalculator.model.AppState
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NAME_SHADOWING")
class AmortizationViewModel : ViewModel() {

    private val _state = MutableLiveData<AppState>()

    val state: LiveData<AppState> get() = _state

    val extraPaymentsMap = mutableMapOf<String, Int>()

    val extraPaymentsLiveData: MutableMap<String, Int> get() = extraPaymentsMap

    var scheduleArrayList: ArrayList<AmortizationResults>? = null

    val scheduleLiveData: MutableLiveData<ArrayList<AmortizationResults>?> = MutableLiveData()

    fun Calculate(amortizationCalculator: AmortizationCalculator) {

        scheduleArrayList?.clear()
        val calculatorResults = (amortizationCalculator.getAmortization(
            state.value!!.loanAmount,
            state.value!!.yearAmount,
            state.value!!.interestAmount,
            state.value!!.downAmount,
            extraPaymentsLiveData
        ))
        val dateArrayList: ArrayList<String>? = ArrayList()

        fun getDates() {
            val cal = Calendar.getInstance()
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val year = cal.get(Calendar.YEAR)
            var startDate = "$month/$day/$year"
            if (state.value!!.dateSimpleFormat != "") {
                startDate = state.value!!.dateSimpleFormat
            }
            //adding total quotas to original month.
            val endDate =
                "" + (month + calculatorResults!!.last().monthId.toInt()) + "/" + day + "/" + year
            println(endDate)
            val inputFormatter = SimpleDateFormat("MM/dd/yyyy")
            val outputFormatter = SimpleDateFormat("MMM yyyy")
            val beginCalendar = Calendar.getInstance()
            val finishCalendar = Calendar.getInstance()
            try {
                beginCalendar.time = inputFormatter.parse(startDate)
                finishCalendar.time = inputFormatter.parse(endDate)
                do {
                    // add one month to date per loop
                    val monthYear = outputFormatter.format(beginCalendar.time)
                    //Log.d("Date_Range", monthYear)
                    beginCalendar.add(Calendar.MONTH, 1)
                    dateArrayList?.add(monthYear)
                } while (beginCalendar.before(finishCalendar))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return
        }
        getDates()
        for (i in 0 until (calculatorResults!!.last().monthId.toInt())) {
            scheduleArrayList?.add(
                AmortizationResults(
                    monthId = dateArrayList!![i],
                    loanLeft = calculatorResults[i].loanLeft,
                    interest = calculatorResults[i].interest,
                    principal = calculatorResults[i].principal,
                    totalInterest = calculatorResults[i].totalInterest,
                    additionalPayment = calculatorResults[i].additionalPayment
                )
            )
        }
        scheduleArrayList?.last()?.totalAmount = calculatorResults[0].totalAmount
        scheduleArrayList?.last()?.monthlyPayment = calculatorResults[0].monthlyPayment
    }


    fun graphProgressBlue() =
        (state.value!!.loanAmount / scheduleArrayList?.last()?.totalAmount!!) * 100

    fun graphProgressPink() = (scheduleArrayList?.last()!!.totalInterest.replace(Regex(","), "")
        .toDouble() / scheduleArrayList?.last()?.totalAmount!!) * 100

    fun getProgress(position: String, total: String) =
        ((position.replace(Regex(","), "").toDouble()) / (total.replace(Regex(","), "")
            .toDouble()))* 100


    private fun getListDetails() {
        populateScheduleList()
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


