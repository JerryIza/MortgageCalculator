package com.example.mortgagecalculator.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.adapters.ScheduleAdapter
import kotlinx.android.synthetic.main.schedule_fragment.*
import kotlinx.android.synthetic.main.schedule_fragment.view.*
import kotlinx.android.synthetic.main.schedule_list.*
import kotlinx.android.synthetic.main.schedule_list.view.*


class ScheduleFragment: Fragment() {

    private lateinit var viewModel: MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }

     override fun onCreateView(inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.schedule_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw  Exception("Invalid Activity")

        viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {
        })

        val jaja = viewModel.scheduleArrayList



        scheduleRecycler.apply {
            //LayoutManager is the same as getLayoutManager() but with getters and setters
            layoutManager = LinearLayoutManager(activity)
            // so we managed to get her done and p
            adapter = ScheduleAdapter(jaja!!)
        }
        updateBtn.setOnClickListener {
        for (i in 0..29) {
            val row: View? = scheduleRecycler.layoutManager?.findViewByPosition(i)
            val textView: TextView? = row?.testYear
            textView?.text = (1 + i).toString()
        }
        }

    }




}