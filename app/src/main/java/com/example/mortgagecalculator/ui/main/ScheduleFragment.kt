package com.example.mortgagecalculator.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.adapters.ScheduleAdapter
import com.example.mortgagecalculator.data.ScheduleOutput
import com.example.mortgagecalculator.databinding.ScheduleFragmentBinding
import kotlinx.android.synthetic.main.schedule_fragment.*

class ScheduleFragment: Fragment() {

    private val caca = listOf(
        ScheduleOutput("test", 3, "hehehehe"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "in pennies"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "go"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "black"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("5", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("10", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "gordos"),
        ScheduleOutput("3", 6, "girl"),
        ScheduleOutput("3", 6, "camel"),
        ScheduleOutput("3", 6, "toe"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "bitch"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "fuck"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "td"),
        ScheduleOutput("3", 6, "tits"),
        ScheduleOutput("10", 6, "ass"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "happy day"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd"),
        ScheduleOutput("3", 6, "kalkjdfslkijfdlkjdfsalkjd")
    )

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
        scheduleRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ScheduleAdapter(caca)
        }
    }

}