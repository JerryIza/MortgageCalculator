package com.example.mortgagecalculator.ui.main.viewmodels

import android.icu.text.SimpleDateFormat
import android.net.ParseException
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mortgagecalculator.db.Input
import com.example.mortgagecalculator.model.AmortizationCalculator
import com.example.mortgagecalculator.model.AmortizationResults
import com.example.mortgagecalculator.repositories.InputRepository
import java.util.*
import kotlin.collections.ArrayList


class AmortizationViewModel @ViewModelInject constructor(
    private val repository: InputRepository
) : ViewModel() {

    private val _inputs = MutableLiveData<Input>()

    val inputs: MutableLiveData<Input> get() = _inputs

    val extraPaymentsMap = mutableMapOf<String, Int>()

    private val extraPaymentsLiveData: MutableMap<String, Int> get() = extraPaymentsMap

    var scheduleArrayList: ArrayList<AmortizationResults>? = null

    val scheduleLiveData: MutableLiveData<ArrayList<AmortizationResults>?> = MutableLiveData()


    fun calculate(amortizationCalculator: AmortizationCalculator) {

        scheduleArrayList?.clear()
        val calculatorResults = (amortizationCalculator.getAmortization(
            inputs.value!!.loanAmount,
            inputs.value!!.yearAmount,
            inputs.value!!.interestAmount,
            inputs.value!!.downAmount,
            extraPaymentsLiveData
        ))
        val dateArrayList: ArrayList<String>? = ArrayList()

        fun getDates() {
            //today's date
            val cal = Calendar.getInstance()
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val year = cal.get(Calendar.YEAR)
            var startDate = "$month/$day/$year"
            //replace today's date with new input if not empty
            if (inputs.value!!.dateSimpleFormat != "") {
                startDate = inputs.value!!.dateSimpleFormat
            }
            val parts = startDate.split("/".toRegex()).toTypedArray()
            //adding total quotas to selected month.
            val endDate = "" + (parts[0] + calculatorResults!!.last().monthId.toInt()) + "/" + parts[1] + "/" + parts[2]

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
            //println("DateArray  " + dateArrayList)
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
        (inputs.value!!.loanAmount / scheduleArrayList?.last()?.totalAmount!!) * 100

    fun graphProgressPink() = (scheduleArrayList?.last()!!.totalInterest.replace(Regex(","), "")
        .toDouble() / scheduleArrayList?.last()?.totalAmount!!) * 100

    fun getProgress(position: String, total: String) =
        ((position.replace(Regex(","), "").toDouble()) / (total.replace(Regex(","), "")
            .toDouble())) * 100


    private fun getListDetails() {
        populateScheduleList()
        scheduleLiveData.value = scheduleArrayList
    }

    private fun populateScheduleList() {
        scheduleArrayList = ArrayList()
    }

    init {
        getListDetails()
        _inputs.value = Input()
    }


    fun deleteAllInputs() = repository.deleteAll()

    suspend fun insertInputs(input: Input): Any {
        return repository.insertInputs(input)
    }

    suspend fun databaseSize(): Int {
        return repository.numberOfItemsInDB()
    }

    val liveInputs = repository.getAllInputs()

}


