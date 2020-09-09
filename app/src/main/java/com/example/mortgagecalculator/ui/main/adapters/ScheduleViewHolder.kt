package com.example.mortgagecalculator.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.databinding.ScheduleListBinding
import com.example.mortgagecalculator.model.AmortizationResults

//separating viewholder makes it cleaner.
class ScheduleViewHolder(
    itemBinding: ScheduleListBinding/*inflater: LayoutInflater, parent: ViewGroup*/,
    parent: ViewGroup
) :
    RecyclerView.ViewHolder(itemBinding.root) {
    //not test
    private var mYearView: TextView? = itemBinding.testYear
    private var mInterestView: TextView? = itemBinding.testInterest
    private var mPrincipalView: TextView? = itemBinding.testPrincipal
    private var mLoanView: TextView? = itemBinding.testLoan
    private var mExtraPayment: TextView? = itemBinding.extraPayment
    private var mPaidInterest: TextView? = null

    fun bind(
        output: AmortizationResults,
        pos: Int,
        listener: (Int) -> Unit
    ) {
        mYearView?.text = output.monthId
        mInterestView?.text = output.interest
        mPrincipalView?.text = output.principal
        mLoanView?.text = output.loanLeft
        mExtraPayment?.text = output.additionalPayment
        mPaidInterest?.text = output.totalInterest
        itemView.setOnClickListener {
            listener(pos)
        }
    }
}



