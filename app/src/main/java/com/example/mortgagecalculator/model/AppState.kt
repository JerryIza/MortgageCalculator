package com.example.mortgagecalculator.model


import java.util.*


data class AppState(
    var loanAmount: Double = 100000.0,
    var yearAmount: Int = 30,
    var interestAmount: Double = 3.50,
    var downAmount: Double = 0.0,
    var dateSimpleFormat: String = "",
    var monthNumber: String = "",
    var extraPayment: Int = 0,
    var recyclerPosition : Int? = null
)
