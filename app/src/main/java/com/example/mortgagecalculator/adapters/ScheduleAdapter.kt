package com.example.mortgagecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.data.ScheduleOutput


class ScheduleAdapter (private val list: List<ScheduleOutput>):
        RecyclerView.Adapter<OutputViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutputViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OutputViewHolder(inflater, parent)

    }

    override fun onBindViewHolder(holder: OutputViewHolder, position: Int) {
        val output: ScheduleOutput = list[position]
        holder.bind(output)
    }

    override fun getItemCount() = list.size
}