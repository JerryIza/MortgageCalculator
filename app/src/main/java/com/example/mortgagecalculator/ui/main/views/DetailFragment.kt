package com.example.mortgagecalculator.ui.main.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mortgagecalculator.databinding.DetailFragmentBinding
import com.example.mortgagecalculator.model.AmortizationCalculator
import com.example.mortgagecalculator.model.AppState
import com.example.mortgagecalculator.ui.main.viewmodels.AmortizationViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.schedule_fragment.*

class DetailFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding

    private lateinit var viewModel: AmortizationViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this).get(AmortizationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding = DetailFragmentBinding.inflate(inflater, container, false)

        val position = viewModel.state.value?.recyclerPosition
        val positionDetails = (position?.let { viewModel.scheduleArrayList?.get(it) })
        val extraPayments = AppState(monthNumber = "", extraPayment = 0)
        val scheduleSize = viewModel.scheduleArrayList!!.size - 1
        val lineChart = binding.lineChart



        binding.calMonth.text = positionDetails?.monthId.toString()
        binding.monthlyInterest.text = positionDetails?.interest + "/" + viewModel.scheduleArrayList?.last()!!.monthlyPayment
        binding.totalInterest.text = positionDetails?.totalInterest + "/" + viewModel.scheduleArrayList?.last()!!.totalInterest
        binding.principal.text = positionDetails?.principal + "/" + viewModel.scheduleArrayList?.last()!!.principal
        binding.loanLeft.text = positionDetails?.loanLeft.toString() + "/" + viewModel.state.value!!.loanAmount.toString()


        binding.moInterestBar.progress = viewModel.getProgress(
            positionDetails!!.interest,
            viewModel.scheduleArrayList?.last()!!.monthlyPayment
        ).toInt()

        binding.interestBar.progress = viewModel.getProgress(
            positionDetails.totalInterest,
            viewModel.scheduleArrayList?.last()!!.totalInterest
        ).toInt()

        binding.moPrincipalBar.progress = viewModel.getProgress(
            positionDetails.principal,
            viewModel.scheduleArrayList?.last()!!.monthlyPayment
        ).toInt()

        binding.loanLeftBar.progress = viewModel.getProgress(
            positionDetails.loanLeft,
            viewModel.state.value!!.loanAmount.toString()
        ).toInt()


        fun saveExtraPayment() {
            val extraAmount = binding.additionalPayment.text.toString().toInt()
            extraPayments.extraPayment = extraAmount.toString().toInt()
            extraPayments.monthNumber = position.toString()
            //saving position value and amount
            viewModel.extraPaymentsMap[position.toString()] = extraPayments.extraPayment
            println("Object Saved: " + viewModel.extraPaymentsMap)
            return
        }


        fun setUpLineChart() {
            val entries1 = ArrayList<Entry>()
            val entries2 = ArrayList<Entry>()
            println("look here maine " + scheduleSize)

            for (i in 0..scheduleSize) {
                entries1.add(Entry( i.toFloat(), viewModel.scheduleArrayList!![i].interest.toFloat()))
                entries2.add(Entry(i.toFloat(), viewModel.scheduleArrayList!![i].principal.toFloat()))
            }

            val dataSet = LineDataSet(entries1, "Interest Curve")
            val dataSet2 = LineDataSet(entries2, "Principal Curve")

            val blueColor = Color.parseColor("#61CFF8")
            val pinkColor = Color.parseColor("#FE657A")
            val greyColor = Color.parseColor("#AEAEAE")

            dataSet.setDrawValues(false)
            dataSet.lineWidth = 3f
            dataSet.setCircleColor(pinkColor)
            dataSet.colors = mutableListOf<Int>(pinkColor, pinkColor)

            dataSet2.setDrawValues(false)
            dataSet2.lineWidth = 3f
            dataSet2.setCircleColor(blueColor)
            dataSet2.highlightLineWidth = 2f
            dataSet2.highLightColor = greyColor
            dataSet2.setDrawHorizontalHighlightIndicator(false)



            lineChart.xAxis.labelRotationAngle = 0f
            lineChart.data = LineData(dataSet, dataSet2)// set data and notifyDataSetChange
            lineChart.invalidate()// refresh chart

            lineChart.axisRight.isEnabled = false
            lineChart.xAxis.axisMaximum = viewModel.scheduleArrayList!!.size.toFloat() - 2

            lineChart.setTouchEnabled(false)
            lineChart.setPinchZoom(false)
            //lineChart.highlighter.getHighlight(0f, viewModel.scheduleArrayList!![0].interest.toFloat())
            lineChart.highlightValue(position.toFloat(), viewModel.scheduleArrayList!![position].interest.toFloat(), 1)

            lineChart.description.text = "Months"

            lineChart.animateX(1800, Easing.EaseInExpo)

        }

        binding.updateBtn.setOnClickListener {
            saveExtraPayment()
            getResults()


        }
        setUpLineChart()
        return binding.root
    }

    private fun getResults() {
        viewModel.Calculate(AmortizationCalculator())
        scheduleRecycler?.adapter?.notifyDataSetChanged()
    }
}





