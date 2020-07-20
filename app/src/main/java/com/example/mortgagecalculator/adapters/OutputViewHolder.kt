package com.example.mortgagecalculator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.model.ScheduleOutput

class OutputViewHolder (inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.schedule_list, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null
    private var mSomethingView: TextView? = null

    init {
        mTitleView = itemView.findViewById(R.id.testLoan)
        mYearView = itemView.findViewById(R.id.testYear)
        mSomethingView = itemView.findViewById(R.id.testYeet)
    }

    fun bind(output : ScheduleOutput)  {
        mTitleView?.text = output.title
        mSomethingView?.text = output.something
        mYearView?.text = output.year.toString()

   }
}



