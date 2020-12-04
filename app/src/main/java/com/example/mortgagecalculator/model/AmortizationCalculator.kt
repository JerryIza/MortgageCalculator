package com.example.mortgagecalculator.model

import android.icu.text.SimpleDateFormat
import android.net.ParseException
import com.example.mortgagecalculator.db.Input
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.pow

object AmortizationCalculator {

    var scheduleArrayList = ArrayList<AmortizationResults>()

    private fun monthlyPayment(
        initialLoanAmount: Double,
        years: Int,
        interest: Double,
        downPayment: Double
    ): Double {
        val i = interest.div(100 * 12)
        val calExponent = (i.plus(1).let {
            years.times(12).let { it1 ->
                it.pow(it1)
            }
        })
        return ((calExponent.let { i.times(it) }).div(calExponent - 1)).let { it ->
            (downPayment.let { initialLoanAmount.minus(it) }).times(it)
        }
    }

    private fun getAmortization(
        initialLoanAmount: Double,
        years: Int,
        interest: Double,
        downPayment: Double,
        extraPayments: MutableMap<String, Int>
    ): ArrayList<AmortizationResults> {

        val scheduleArrayList: ArrayList<AmortizationResults> = ArrayList()
        val items: HashMap<String, Int> = HashMap(extraPayments)

        scheduleArrayList.clear()
        val moPayment = monthlyPayment(initialLoanAmount, years, interest, downPayment)
        val numberQuotas = (years * 12)
        var newInterestPayment = ((interest.div(100 * 12)) * (initialLoanAmount - downPayment))
        var newPrincipal = moPayment - newInterestPayment
        var newLoan = (initialLoanAmount - downPayment) - newPrincipal
        var paidInterest = newInterestPayment
        for (i in 0..numberQuotas) {
            //Matching keys with loop integer for extra payments,
            val match: Boolean = items.keys.stream()
                .mapToInt { k -> k.toInt() }
                .anyMatch { k -> k == i }
            //iterate through keys and if it matches key, add extra payment, then add additional payment where nessesary.
            var additionalPayment = ""
            if (match) {
                //make double so we can format text,
                val extraPayment: Double? = (items[i.toString()])?.toDouble()
                newLoan -= extraPayment!!
                additionalPayment = "EP"
            }
            scheduleArrayList.add(
                AmortizationResults(
                    monthId = (i + 1).toString(),
                    loanLeft = String.format("%,.2f", newLoan),
                    interest = String.format("%,.2f", newInterestPayment),
                    principal = String.format("%,.2f", newPrincipal),
                    totalInterest = String.format("%,.2f", paidInterest),
                    additionalPayment = additionalPayment
                )
            )
            //updating vars happens every time the loop is run
            newInterestPayment = ((interest.div(100 * 12)) * newLoan)
            newPrincipal = moPayment - newInterestPayment
            newLoan -= newPrincipal
            paidInterest += newInterestPayment
            //when loan is paid off and becomes negative, run the last calculated results from above and set loan to zero.
            if (newLoan < 1) {
                newPrincipal += newLoan
                scheduleArrayList.add(
                    AmortizationResults(
                        monthId = (i + 2).toString(),
                        loanLeft = "0",
                        interest = String.format("%,.2f", newInterestPayment),
                        principal = String.format("%,.2f", newPrincipal),
                        totalInterest = String.format("%,.2f", paidInterest)
                    )
                )
                break
            }
        }
        //need to remove "," other wise parsing will fail
        val formattedInterest = scheduleArrayList.last().totalInterest.replace(Regex(","), "")
        scheduleArrayList[0].totalAmount = (formattedInterest.toDouble() + initialLoanAmount)
        scheduleArrayList[0].monthlyPayment = String.format("%,.2f", moPayment)

        return scheduleArrayList
    }


    fun calculate(inputs: Input): ArrayList<AmortizationResults> {

        scheduleArrayList.clear()
        val calculatorResults = getAmortization(
            inputs.loanAmount,
            inputs.yearAmount,
            inputs.interestAmount,
            inputs.downAmount,
            inputs.payments
        )
        val dateArrayList: ArrayList<String> = ArrayList()
        fun getDates() {
            //today's date
            val cal = Calendar.getInstance()
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val year = cal.get(Calendar.YEAR)
            var startDate = "$month/$day/$year"
            //replace today's date with new input if not empty
            if (inputs.startDateFormat != "") {
                startDate = inputs.startDateFormat
            }

            val parts = startDate.split("/".toRegex()).toTypedArray()
            //adding total quotas to selected month.
            val endDate =
                "" + (parts[0] + calculatorResults.last().monthId.toInt()) + "/" + parts[1] + "/" + parts[2]
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
                    dateArrayList.add(monthYear)
                } while (beginCalendar.before(finishCalendar))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return
        }

        getDates()
        for (i in 0 until (calculatorResults.last().monthId.toInt())) {
            scheduleArrayList.add(
                AmortizationResults(
                    monthId = dateArrayList[i],
                    loanLeft = calculatorResults[i].loanLeft,
                    interest = calculatorResults[i].interest,
                    principal = calculatorResults[i].principal,
                    totalInterest = calculatorResults[i].totalInterest,
                    additionalPayment = calculatorResults[i].additionalPayment
                )
            )
        }
        scheduleArrayList.last().totalAmount = calculatorResults[0].totalAmount
        scheduleArrayList.last().monthlyPayment = calculatorResults[0].monthlyPayment


        return scheduleArrayList
    }


    fun calculatePieProgress(inputs: Input): Double? {
        var result: Double? = null
        if (!scheduleArrayList.isNullOrEmpty()) {
            result =
                scheduleArrayList.last().totalAmount.let { inputs.loanAmount.div(it!!).times(100) }
        }
        return result
    }


}