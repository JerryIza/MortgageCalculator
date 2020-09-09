package com.example.mortgagecalculator.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.databinding.ScheduleListBinding
import com.example.mortgagecalculator.model.AmortizationResults

class ScheduleAdapter(
    private val list: List<AmortizationResults>,
    private val listener: (Int) -> Unit
) :
    RecyclerView.Adapter<ScheduleViewHolder>() {

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



