package com.example.mortgagecalculator.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mortgagecalculator.databinding.ScheduleFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.*

class ScheduleFragment: Fragment() {



    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManger: RecyclerView.LayoutManager

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val binding = ScheduleFragmentBinding.inflate(inflater)

            viewAdapter = LinearLayoutManager(this)
            viewAdapter = ScheduleAdapter(myDataset)

            recyclerView = binding.scheduleRecycler{
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter


            }


        return binding.root

    }

}