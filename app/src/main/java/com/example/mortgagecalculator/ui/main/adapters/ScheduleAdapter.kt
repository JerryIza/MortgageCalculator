package com.example.mortgagecalculator.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.databinding.ScheduleListBinding
import com.example.mortgagecalculator.model.AmortizationResults

class ScheduleAdapter(
    private val list: ArrayList<AmortizationResults>,
    private val listener: (Int) -> Unit
) :
    RecyclerView.Adapter<ScheduleViewHolder>() {

    fun setItems(inputs: ArrayList<AmortizationResults>) {
        this.list.clear()
        this.list.addAll(inputs)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        //val inflater = LayoutInflater.from(parent.context)
        val binding: ScheduleListBinding = ScheduleListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        //return ScheduleViewHolder(inflater, parent)
        return ScheduleViewHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) =
        holder.bind(list[position], position, listener)

    override fun getItemCount() = list.size

}

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



