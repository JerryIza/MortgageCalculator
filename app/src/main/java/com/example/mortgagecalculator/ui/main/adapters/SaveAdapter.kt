package com.example.mortgagecalculator.ui.main.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.databinding.SaveListBinding
import com.example.mortgagecalculator.db.Input
import com.example.mortgagecalculator.model.AmortizationCalculator.scheduleArrayList

class SaveAdapter(
    context: Context,
    private val inputs: ArrayList<Input>,
    private val listener: (Int) -> Unit
) :
    RecyclerView.Adapter<SaveViewHolder>() {
    //private var mContext: Context? = context


    fun setItems(inputs: ArrayList<Input>) {
        this.inputs.clear()
        this.inputs.addAll(inputs)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Input{
        return inputs[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveViewHolder {
        val binding: SaveListBinding =
            SaveListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SaveViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaveViewHolder, position: Int) {
        holder.bind(inputs[position], position, listener)

    }
    override fun getItemCount() = inputs.size

}

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

        if(input.payments.isNotEmpty())
        {
            mPaymentsView?.text = ("Includes Extra Payments:  Yes")}
        else {
            mPaymentsView?.text = ("Includes Extra Payments:  No")
        }

        itemView.setOnClickListener {
            listener(pos)
        }
    }




}

