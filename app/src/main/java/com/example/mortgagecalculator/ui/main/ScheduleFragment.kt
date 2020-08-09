package com.example.mortgagecalculator.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.adapters.OutputViewHolder
import com.example.mortgagecalculator.adapters.ScheduleAdapter
import com.example.mortgagecalculator.model.ScheduleOutput
import kotlinx.android.synthetic.main.schedule_fragment.*


class ScheduleFragment : Fragment() {



    private lateinit var viewModel: MainViewModel

     override fun onCreateView(inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.schedule_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        val scheduleList = viewModel.scheduleArrayList

        scheduleRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ScheduleAdapter(scheduleList!!){
                Toast.makeText(context, scheduleList.get(it).totalInterest, Toast.LENGTH_SHORT).show()

            }
        }
        //get last value
        /*textView7.text = scheduleList!!.get(359).totalInterest.toString()
        viewModel.scheduleArrayList!!.get(359).totalInterest*/
    }

}


