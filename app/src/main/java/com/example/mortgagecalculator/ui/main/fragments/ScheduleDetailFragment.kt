package com.example.mortgagecalculator.ui.main.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mortgagecalculator.databinding.DetailFragmentBinding
import com.example.mortgagecalculator.model.AmortizationResults
import com.example.mortgagecalculator.ui.main.viewmodels.AmortizationViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.schedule_fragment.*


class ScheduleDetailFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding

    private lateinit var viewModel: AmortizationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = activity?.run {
            ViewModelProvider(this).get(AmortizationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        val position = viewModel.inputs.value?.epPosition
        val positionDetails = (position?.let { viewModel.scheduleArrayList?.get(it) })
        setUpObservers()

        val maximumExtraPayment = positionDetails?.loanLeft!!.replace(Regex(","), "").toFloat()

        (binding.additionalPayment as EditText).setOnEditorActionListener { _, actionId, _ ->
            //payment does not exceed loan left.
            //stream keys and check for no key and prevent updateResults
            if (binding.additionalPayment.text!!.isNotEmpty() && actionId == EditorInfo.IME_ACTION_DONE && binding.additionalPayment.text.toString()
                    .toInt() > maximumExtraPayment
            ) {
                binding.downTextInput.error = "Amount cannot exceed loan left"
            }
            //input and get results
            else if (actionId == EditorInfo.IME_ACTION_DONE && binding.additionalPayment.text!!.isNotEmpty()) {
                saveExtraPayment()
                updateResults()
                binding.additionalPayment.setSelection(binding.additionalPayment.length())
            } else {
                //if empty, delete key for extra payment
                viewModel.inputs.value!!.payments.remove(position.toString())
                updateResults()
            }
            false
        }
        return binding.root
    }

    private fun setUpObservers() {
        viewModel.scheduleLiveData.observe(viewLifecycleOwner, {
            bindResults(it)
        })

    }

    private fun bindResults(amortizationResults: ArrayList<AmortizationResults>?, ) {
        val position = viewModel.inputs.value?.epPosition
        val positionDetails =
            (viewModel.inputs.value?.epPosition?.let { viewModel.scheduleArrayList?.get(it) })

        val cumulativePrincipal =
            (viewModel.inputs.value!!.loanAmount - positionDetails!!.loanLeft.replace(
                Regex(","),
                ""
            ).toDouble())

        binding.calMonth.text = positionDetails.monthId
        binding.monthlyInterest.text =
            (positionDetails.interest + " / " + amortizationResults!!.last().monthlyPayment)
        binding.principal.text =
            (positionDetails.principal + " / " + amortizationResults.last().monthlyPayment)
        binding.totalInterest.text =
            (positionDetails.totalInterest + " / " + amortizationResults.last().totalInterest)
        binding.totalPrincipal.text =
            (String.format("%,.2f", cumulativePrincipal) + " / " + String.format(
                "%,.2f",
                viewModel.inputs.value!!.loanAmount
            ))

        binding.loanLeft.text = (positionDetails.loanLeft + " / " + String.format(
            "%,.2f",
            viewModel.inputs.value!!.loanAmount
        ))

        binding.moInterestBar.progress = viewModel.getProgress(
            positionDetails.interest,
            amortizationResults.last().monthlyPayment
        ).toInt()

        binding.moPrincipalBar.progress = viewModel.getProgress(
            positionDetails.principal,
            amortizationResults.last().monthlyPayment
        ).toInt()

        binding.interestBar.progress = viewModel.getProgress(
            positionDetails.totalInterest,
            amortizationResults.last().totalInterest
        ).toInt()

        binding.totalPrincipalBar.progress = viewModel.getProgress(
            (cumulativePrincipal.toString()),
            viewModel.inputs.value!!.loanAmount.toString()
        ).toInt()

        binding.loanLeftBar.progress = viewModel.getProgress(
            positionDetails.loanLeft,
            viewModel.inputs.value!!.loanAmount.toString()
        ).toInt()
        //set text
        if (positionDetails.additionalPayment.isNotEmpty()) {
            binding.additionalPayment.setText(viewModel.inputs.value!!.payments[position.toString()].toString())
        }

        setUpLineChart(amortizationResults)
    }

    private fun setUpLineChart(entries: ArrayList<AmortizationResults>?) {
        val lineChart = binding.lineChart
        val scheduleSize = entries!!.size - 1
        val entries1 = ArrayList<Entry>()
        val entries2 = ArrayList<Entry>()
        for (i in 0..scheduleSize) {
            entries1.add(
                Entry(
                    i.toFloat(), entries[i].interest.replace(
                        Regex(
                            ","
                        ), ""
                    ).toFloat()
                )
            )
            entries2.add(
                Entry(
                    i.toFloat(), entries[i].principal.replace(
                        Regex(
                            ","
                        ), ""
                    ).toFloat()
                )
            )
        }

        val interestData = LineDataSet(entries1, "Interest")
        val principalData = LineDataSet(entries2, "Principal")

        val blueColor = Color.parseColor("#61CFF8")
        val pinkColor = Color.parseColor("#FE657A")
        val greyColor = Color.parseColor("#AEAEAE")

        interestData.setDrawValues(false)
        interestData.lineWidth = 3f
        interestData.setCircleColor(pinkColor)
        interestData.color = pinkColor

        principalData.setDrawValues(false)
        principalData.lineWidth = 3f
        principalData.setCircleColor(blueColor)
        principalData.highlightLineWidth = 2f
        principalData.highLightColor = greyColor
        principalData.setDrawHorizontalHighlightIndicator(false)

        lineChart.xAxis.labelRotationAngle = 0f
        lineChart.data =
            LineData(interestData, principalData)// set data and notifyDataSetChange
        lineChart.invalidate()// refresh chart

        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.axisMaximum = viewModel.scheduleArrayList!!.size.toFloat() - 2

        lineChart.setTouchEnabled(false)
        lineChart.setPinchZoom(false)
        lineChart.highlightValue(
            viewModel.inputs.value?.epPosition!!.toFloat(),
            entries[viewModel.inputs.value?.epPosition!!].interest.replace(
                Regex(
                    ","
                ), ""
            ).toFloat(),
            1
        )
        lineChart.description.text = "Months"
        lineChart.animateX(1800, Easing.EaseInExpo)
    }

    private fun saveExtraPayment() {
        val extraAmount = binding.additionalPayment.text.toString()
        binding.additionalPayment.setText(extraAmount)
        //set key and value
        viewModel.inputs.value?.payments?.set(
            viewModel.inputs.value?.epPosition.toString(),
            (extraAmount.toInt())
        )
        return
    }

    private fun updateResults() {
        viewModel.getCalculationResults()
        scheduleRecycler?.adapter?.notifyDataSetChanged()
    }
}





