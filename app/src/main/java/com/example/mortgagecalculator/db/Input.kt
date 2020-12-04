package com.example.mortgagecalculator.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "inputs_table")
data class Input @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "savedInputsTitle")
    var savedInputsTitle: String = "",
    @ColumnInfo(name = "created_at")
    var createdAt: Long = 0,
    @ColumnInfo(name = "modified_at")
    var modifiedAt: Long = 0,
    var loanAmount: Double =200000.0,
    var yearAmount: Int = 0,
    var yearSpinnerPos: Int = 1,
    var interestAmount: Double = 3.50,
    var downAmount: Double = 0.0,
    var startDateFormat: String = "",
    var endDateFormat: String = "",
    var epPosition: Int? = null,
    var extraPaymentSize: String = "0",
    @TypeConverters(Converter::class)
    var payments: MutableMap<String, Int> = mutableMapOf()
)
