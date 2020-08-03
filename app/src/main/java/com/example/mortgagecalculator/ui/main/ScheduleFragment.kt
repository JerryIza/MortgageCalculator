package com.example.mortgagecalculator.ui.main

import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.adapters.ScheduleAdapter
import com.example.mortgagecalculator.model.MortgageDefaults
import com.example.mortgagecalculator.model.ScheduleOutput
import kotlinx.android.synthetic.main.schedule_fragment.*
import kotlinx.android.synthetic.main.schedule_list.*
import kotlinx.android.synthetic.main.schedule_list.view.*
import java.util.ArrayList


class ScheduleFragment: Fragment() {






    private lateinit var viewModel: MainViewModel

     override fun onCreateView(inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.schedule_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run { ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        val jaja = viewModel.scheduleArrayList

        scheduleRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ScheduleAdapter(jaja!!)

        }

    }

}


