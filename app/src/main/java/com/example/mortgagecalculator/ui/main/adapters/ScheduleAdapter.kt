package com.example.mortgagecalculator.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.model.AmortizationResults
import java.util.*
import kotlin.collections.ArrayList

//RecyclerView with multiple view types
class ScheduleAdapter(private val list: List<AmortizationResults>, private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<OutputViewHolder>() {

    private var outputList = arrayListOf<AmortizationResults>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutputViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OutputViewHolder(inflater, parent)
    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: OutputViewHolder, position: Int) =
        holder.bind(list[position], position, listener)

    //this right here
    fun setResults(outputList: List<AmortizationResults>) {
        this.outputList.clear()
        this.outputList.addAll(outputList)
        notifyDataSetChanged()
    }


}



