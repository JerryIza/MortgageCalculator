package com.example.mortgagecalculator.utils

import android.icu.text.SimpleDateFormat
import android.net.ParseException
import com.example.mortgagecalculator.db.Input
import java.util.*

object ExtraPayments {

    fun recurringPayments(excludedList: ArrayList<String>?, inputs: Input): Int {
        //will exclude this range from additional payments.
        val startDate = inputs.startDateFormat
        val endDate = inputs.endDateFormat
        val inputFormatter = SimpleDateFormat("MM/dd/yyyy")
        val outputFormatter = SimpleDateFormat("MMM yyyy")
        val beginCalendar = Calendar.getInstance()
        val finishCalendar = Calendar.getInstance()
        try {
            beginCalendar.time = inputFormatter.parse(startDate)
            finishCalendar.time = inputFormatter.parse(endDate)
            do {
                val monthYear = outputFormatter.format(beginCalendar.time)

                // add one month to date per loop
                println(monthYear)

                beginCalendar.add(Calendar.MONTH, 1)
                excludedList?.add(monthYear)

            } while (beginCalendar.before(finishCalendar))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return if (excludedList!!.first() != excludedList.last()) {
            excludedList.size
        } else {
            excludedList.size - 1
        }
    }


}