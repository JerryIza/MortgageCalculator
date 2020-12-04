package com.example.mortgagecalculator.utils

import android.icu.text.SimpleDateFormat
import android.net.ParseException
import com.example.mortgagecalculator.db.Input
import java.util.*
import kotlin.collections.ArrayList

object ExtraPayments {

    fun recurringPayments(dateArrayList: ArrayList<String>?, inputs: Input) {
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
}