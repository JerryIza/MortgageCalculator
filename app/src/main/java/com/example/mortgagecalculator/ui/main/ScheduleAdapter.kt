package com.example.mortgagecalculator.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.R

class ScheduleAdapter (private val myDataset: Array<String>):
        RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>() {

    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapter.MyViewHolder {

        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.schedule_fragment,parent,false) as TextView

        return MyViewHolder(textView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = myDataset[position]
    }

    override fun getItemCount() = myDataset.size
}