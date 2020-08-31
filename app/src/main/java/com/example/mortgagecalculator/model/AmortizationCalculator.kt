package com.example.mortgagecalculator.model

import kotlin.math.pow

class AmortizationCalculator {


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

    fun getAmortization(
        initialLoanAmount: Double,
        years: Int,
        interest: Double,
        downPayment: Double,
        extraPayments: MutableMap<String, Int>
    ): ArrayList<AmortizationResults>? {

        val scheduleArrayList: ArrayList<AmortizationResults>? = ArrayList()
        val items: HashMap<String, Int> = HashMap(extraPayments)

        scheduleArrayList?.clear()
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
                newLoan -= (newPrincipal + extraPayment!!)
                additionalPayment = "EP"
            }
            scheduleArrayList?.add(
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
                scheduleArrayList?.add(
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
        val formattedInterest = scheduleArrayList!!.last().totalInterest.replace(Regex(","), "")
        scheduleArrayList[0].totalAmount = (formattedInterest.toDouble() + initialLoanAmount)
        scheduleArrayList[0].monthlyPayment = String.format("%,.2f", moPayment)

        println(scheduleArrayList)
        return scheduleArrayList
    }

}