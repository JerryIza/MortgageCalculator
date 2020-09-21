package com.example.mortgagecalculator.ui.main.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.databinding.SaveListBinding
import com.example.mortgagecalculator.db.Input

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

