package com.example.mortgagecalculator.model

data class AmortizationResults(
    var monthId: String = "",
    var loanLeft: String = "",
    var interest: String= "",
    var principal: String= "",
    var totalInterest: String= "",
    var totalAmount: Double? = null,
    var monthlyPayment: String = "",
    var additionalPayment: String = ""
)





