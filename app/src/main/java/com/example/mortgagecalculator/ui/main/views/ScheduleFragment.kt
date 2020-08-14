package com.example.mortgagecalculator.ui.main.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.ui.main.viewmodels.MainViewModel
import com.example.mortgagecalculator.ui.main.adapters.ScheduleAdapter
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
            println("recyclerview")
            layoutManager = LinearLayoutManager(activity)
            adapter = ScheduleAdapter(scheduleList!!){
                findNavController().navigate(R.id.detailFragment)
                viewModel.state.value?.recyclerPosition = it
                          //viewModel.state.value?.recyclerPosition = scheduleList[it].totalInterest.toInt()

            }
        }
/*viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {})*/

    }

}


