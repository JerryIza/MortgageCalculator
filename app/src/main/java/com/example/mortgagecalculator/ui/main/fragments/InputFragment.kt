package com.example.mortgagecalculator.ui.main.fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mortgagecalculator.databinding.InputFragmentBinding
import com.example.mortgagecalculator.model.AmortizationResults
import com.example.mortgagecalculator.ui.main.viewmodels.AmortizationViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.schedule_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class InputFragment : Fragment() {

    private lateinit var binding: InputFragmentBinding

    lateinit var viewModel: AmortizationViewModel

    //Getting Today's Date
    private val cal: Calendar = Calendar.getInstance()
    private val month = cal.get(Calendar.MONTH)
    private val day = cal.get(Calendar.DAY_OF_MONTH)
    private val year = cal.get(Calendar.YEAR)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InputFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this).get(AmortizationViewModel::class.java)
        } ?: throw Exception("invalid Activity")
        lifecycleScope.launch { setUpInputs() }

    }


    fun getResults() {
        viewModel.getCalculationResults()
        scheduleRecycler?.adapter?.notifyDataSetChanged()
        setupObservers()
    }

    private fun editTextLoan(inputNumber: EditText) {
        inputNumber.setOnEditorActionListener { _, actionId, _ ->
            if (inputNumber.text!!.isNotEmpty() && actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.inputs.value?.loanAmount =
                    inputNumber.text.replace(Regex(","), "").toDouble()
                getResults()
                binding.mortgageLoan.setSelection(binding.mortgageLoan.length())

            } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.inputs.value?.loanAmount = 0.0
                getResults()
            }
            false
        }
    }

    private fun editTextInterest(inputNumber: EditText) {
        inputNumber.setOnEditorActionListener { _, actionId, _ ->
            if (inputNumber.text!!.isNotEmpty() && actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.inputs.value?.interestAmount =
                    inputNumber.text.replace(Regex(","), "").toDouble()
                //setting new var values to dataclass in ViewModel State.
                getResults()
                binding.mortgageInterest.setSelection(binding.mortgageInterest.length())

            } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.inputs.value?.interestAmount = 0.0
                getResults()
            }
            false
        }
    }

    private fun editTextDownPayment(inputNumber: EditText) {
        inputNumber.setOnEditorActionListener { _, actionId, _ ->
            if (inputNumber.text!!.isNotEmpty() && actionId == EditorInfo.IME_ACTION_DONE) {

                viewModel.inputs.value?.downAmount =
                    inputNumber.text.replace(Regex(","), "").toDouble()

                //setting new var values to dataclass in ViewModel State.
                getResults()
                try {
                    binding.mortgageDown.setSelection(binding.mortgageInterest.length())
                } catch (e: IndexOutOfBoundsException) {
                }

            } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.inputs.value?.downAmount = 0.0
                getResults()
            }
            false
        }
    }

    private fun selectYear() {
        binding.yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val s: Int = parent?.getItemAtPosition(position).toString().toInt()
                viewModel.inputs.value?.yearSpinnerPos = position
                viewModel.inputs.value?.yearAmount = s
                getResults()
            }
        }
    }

    private suspend fun setUpInputs() {
        //loading db inputs once
        if (viewModel.inputs.value?.loanAmount == 0.0) {
            lifecycleScope.launch(Dispatchers.Main) {
                if (viewModel.databaseSize() != 0) {
                    viewModel.inputs.value = viewModel.getInputs()[0]
                }
                viewModel.inputs.value?.yearSpinnerPos?.let { binding.yearSpinner.setSelection(it) }
                editTextLoan(binding.mortgageLoan)
                editTextInterest(binding.mortgageInterest)
                editTextDownPayment(binding.mortgageDown)
                selectYear()
                pickDate()
            }
        } else {
            viewModel.inputs.value?.yearSpinnerPos?.let { binding.yearSpinner.setSelection(it) }
            editTextLoan(binding.mortgageLoan)
            editTextInterest(binding.mortgageInterest)
            editTextDownPayment(binding.mortgageDown)
            selectYear()
            pickDate()
        }
    }

    private fun pickDate() {
        val datePicker = binding.startBtn
        datePicker.setOnClickListener {
            val cal = Calendar.getInstance()
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val year = cal.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, mYear, mMonth, mDay ->
                    datePicker.text = ("" + (mMonth + 1) + "/" + mDay + "/" + mYear)
                    //both are instances not raw ints
                    viewModel.inputs.value?.startDateFormat =
                        "" + (mMonth + 1) + "/" + mDay + "/" + mYear
                },
                //Dialog start
                year,
                month,
                day
            )
            datePickerDialog.show()
            datePickerDialog.setOnDismissListener {
                getResults()
            }
        }
    }

    private fun setupObservers() {
        viewModel.scheduleLiveData.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                bindResults(it.last())
                setUpPieChart()
            }
        })
    }


    private fun bindResults(amortizationResults: AmortizationResults) {

        if (viewModel.inputs.value!!.loanAmount == 0.0) {
            binding.mortgageLoan.setText("")
        } else {
            binding.mortgageLoan.setText(
                String.format(
                    "%,.2f",
                    viewModel.inputs.value!!.loanAmount
                )
            )
        }

        if (viewModel.inputs.value!!.interestAmount == 0.0) {
            binding.mortgageInterest.setText("")
        } else {
            binding.mortgageInterest.setText(
                String.format(
                    "%,.2f",
                    viewModel.inputs.value!!.interestAmount
                )
            )
        }

        if (viewModel.inputs.value!!.downAmount == 0.0) {
            binding.mortgageDown.setText("")
        } else {
            binding.mortgageDown.setText(
                String.format(
                    "%,.2f",
                    viewModel.inputs.value!!.downAmount
                )
            )
        }

        binding.monthlyPayment.text = ("$${amortizationResults.monthlyPayment}")
        binding.numberofPayments.text = viewModel.scheduleArrayList!!.size.toString()
        binding.payOffDate.text = amortizationResults.monthId
        binding.totalInterest.text = ("$${amortizationResults.totalInterest}")
        binding.totalAmount.text = ("$${String.format("%,.2f", amortizationResults.totalAmount)}")
        //Setting up todays date
        if (viewModel.inputs.value!!.startDateFormat == "") {
            binding.startBtn.text = ("${month + 1}/$day/$year")
            viewModel.inputs.value!!.startDateFormat = ("${month + 1}/$day/$year")
        } else {
            //Put today's or custom selected date in Model... ready to save.
            binding.startBtn.text = viewModel.inputs.value!!.startDateFormat
        }
        setUpPieChart()
    }

    private fun setUpPieChart() {
        val chart = binding.pieChart
        val transparentColor = Color.parseColor("#434343")
        val holeColor = Color.parseColor("#40F3F3F3")

        chart.setUsePercentValues(true)
        chart.isRotationEnabled = false
        chart.setTouchEnabled(false)
        chart.description.isEnabled
        chart.setExtraOffsets(0f, 0f, 0f, -15f)
        chart.setDrawEntryLabels(false)
        chart.isDrawHoleEnabled
        chart.setHoleColor(holeColor)
        chart.holeRadius = 60f
        chart.transparentCircleRadius = 65f
        chart.setTransparentCircleColor(transparentColor)
        chart.setTransparentCircleAlpha(9000)
        chart.description.text = ""
        val yValue = ArrayList<PieEntry>()

        viewModel.getPieChartProgress()?.let { PieEntry(it.toFloat(), "Interest %") }?.let {
            yValue.add(
                PieEntry(100 - it.value, "Interest %")
            )
        }
        viewModel.getPieChartProgress()?.let { PieEntry(it.toFloat(), "Principal %") }?.let {
            yValue.add(
                it
            )
        }

        val blueColor = Color.parseColor("#61CFF8")
        val pinkColor = Color.parseColor("#FE657A")
        val dataSet = PieDataSet(yValue, "")
        dataSet.colors = mutableListOf<Int>(pinkColor, blueColor)
        val pieData = PieData(dataSet)
        pieData.setValueTextSize(12f)
        pieData.setValueTextColor(Color.WHITE)
        chart.data = pieData // set data and notifyDataSetChange
        chart.invalidate() // refresh chart
    }

}


