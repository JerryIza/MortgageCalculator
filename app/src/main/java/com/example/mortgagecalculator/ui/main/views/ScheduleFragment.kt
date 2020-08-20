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
import com.example.mortgagecalculator.databinding.ScheduleFragmentBinding
import com.example.mortgagecalculator.ui.main.adapters.ScheduleAdapter
import com.example.mortgagecalculator.ui.main.viewmodels.AmortizationViewModel
import kotlinx.android.synthetic.main.schedule_fragment.*

class ScheduleFragment : Fragment() {

    private lateinit var binding: ScheduleFragmentBinding


    private lateinit var viewModel: AmortizationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ScheduleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this).get(AmortizationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val scheduleList = viewModel.scheduleArrayList

        scheduleRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ScheduleAdapter(scheduleList!!) {
                findNavController().navigate(R.id.detailFragment)
                //it:int equals get position clicked. we are saving position in state and pulling position in detail fragment again
                viewModel.state.value?.recyclerPosition = it
            }
        }
    }
}







