package com.example.mortgagecalculator.model

data class AmortizationResults(
    var quotas: String,
    var loanLeft: String,
    var interest: String,
    var principal: String,
    var totalInterest: String,
    var totalAmount: String? = null
)




