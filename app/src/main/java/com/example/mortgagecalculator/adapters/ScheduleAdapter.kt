package com.example.mortgagecalculator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.databinding.MainFragmentBinding.inflate
import com.example.mortgagecalculator.model.ScheduleOutput
import kotlinx.android.synthetic.main.schedule_list.view.*

//RecyclerView with multiple view types
class ScheduleAdapter(private val list: List<ScheduleOutput>): RecyclerView.Adapter<OutputViewHolder>() {

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