package com.example.mortgagecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.model.ScheduleOutput

//RecyclerView with multiple view types
class ScheduleAdapter(private val list: List<ScheduleOutput>, val listener: (Int) -> Unit): RecyclerView.Adapter<OutputViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutputViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OutputViewHolder(inflater, parent)
    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: OutputViewHolder, position: Int)=
        holder.bind(list[position], position , listener)



}


