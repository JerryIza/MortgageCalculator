package com.example.mortgagecalculator.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inputs_table")
data class Input(
    var loanAmount: Double = 100000.0,
    var yearAmount: Int = 0,
    var yearSpinnerPos: Int = 1,
    var interestAmount: Double = 3.50,
    var downAmount: Double = 0.0,
    var dateSimpleFormat: String = "",
    var monthNumber: String = "",
    var extraPayment: Int = 0,
    var recyclerPosition : Int? = null
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}