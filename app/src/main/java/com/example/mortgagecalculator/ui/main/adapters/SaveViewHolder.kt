package com.example.mortgagecalculator.ui.main.adapters

import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.databinding.SaveListBinding
import com.example.mortgagecalculator.db.Input
import org.w3c.dom.Text


class SaveViewHolder(itemBinding: SaveListBinding) : RecyclerView.ViewHolder(
    itemBinding.root
) {
    private var mTitleView: TextView? = itemBinding.saveTitleName
    private var mLoanView: TextView? = itemBinding.saveLoanAmount
    private var mYearView: TextView? = itemBinding.saveYearAmount
    private var mInterestView: TextView? = itemBinding.saveInterestAmount
    private var mDownView: TextView? = itemBinding.saveDownAmount
    private var mDSFView: TextView? = itemBinding.saveDSF
    private var mPaymentsView: TextView? = itemBinding.saveExtraPayments


    fun bind(
        input: Input,
        pos: Int,
        listener: (Int) -> Unit
    ){
        mTitleView?.text = input.savedInputsTitle
        mDSFView?.text = ("Start Date: " + input.startDateFormat)
        mLoanView?.text = ("Loan: "+ "$"+ String.format("%,.2f", input.loanAmount))
        mYearView?.text = ("Years: " + input.yearAmount.toString())
        mInterestView?.text = ("Interest: "+ input.interestAmount.toString() + "%")
        mDownView?.text = ("Money Down: "+ "$"+ String.format("%,.2f", input.downAmount))
        mPaymentsView?.text = ("Number of Extra Payments: ${input.extraPaymentSize}")
        println(input.extraPaymentSize)
        itemView.setOnClickListener {
            listener(pos)
        }
    }

}





