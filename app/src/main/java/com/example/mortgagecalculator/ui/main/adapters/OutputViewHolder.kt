package com.example.mortgagecalculator.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.model.AmortizationResults

class OutputViewHolder (inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.schedule_list, parent, false)) {
    private var mYearView: TextView? = null
    private var mInterestView: TextView? = null
    private var mPrincipalView: TextView? = null
    private var mLoanView: TextView? = null
    private var mExtraPayment: TextView? = null
    private var mPaidInterest: TextView? = null


    init {
        mYearView = itemView.findViewById(R.id.testYear)
        mInterestView = itemView.findViewById(R.id.testInterest)
        mPrincipalView = itemView.findViewById(R.id.testPrincipal)
        mLoanView = itemView.findViewById(R.id.testLoan)
        mExtraPayment = itemView.findViewById(R.id.extraPayment)
    }

    fun bind(output: AmortizationResults,
             pos: Int,
             listener: (Int) -> Unit)  {
        mYearView?.text = output.quotas
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



