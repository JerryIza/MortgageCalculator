package com.example.mortgagecalculator.ui.main.views

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mortgagecalculator.databinding.InputFragmentBinding
import com.example.mortgagecalculator.model.AmortizationCalculator
import com.example.mortgagecalculator.model.AmortizationResults
import com.example.mortgagecalculator.ui.main.viewmodels.AmortizationViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.input_fragment.*
import kotlinx.android.synthetic.main.schedule_fragment.*
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class InputFragment : Fragment() {

    private lateinit var binding: InputFragmentBinding

    lateinit var viewModel: AmortizationViewModel

    //Getting Today's Date
    val cal = Calendar.getInstance()
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val year = cal.get(Calendar.YEAR)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InputFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this).get(AmortizationViewModel::class.java)
        } ?: throw Exception("invalid Activity")

        val textDate = binding.startBtn
        var initialLoanAmount: Double
        var years: Int
        var interest: Double
        var downPayment: Double

        val fullDecimalFormat = DecimalFormat("#,###.######")

        //getting data class variables from ViewModel state, and formatting once ran once
        initialLoanAmount = viewModel.inputs.value!!.loanAmount
        val loanInput: String = initialLoanAmount.toString()
        mortgageLoan.setText(loanInput)

        viewModel.inputs.value?.yearSpinnerPos?.let { binding.yearSpinner.setSelection(it) }


        interest = viewModel.inputs.value!!.interestAmount
        val interestInput: String = String.format("%,.2f", interest)
        mortgageInterest.setText(interestInput)

        downPayment = viewModel.inputs.value!!.downAmount
        val totalPaid = downPayment
        mortgageDown.setText(fullDecimalFormat.format(totalPaid))


        textDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val year = cal.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, mYear, mMonth, mDay ->
                    textDate.text = ("" + (mMonth + 1) + "/" + mDay + "/" + mYear)
                    //both are instances not raw ints
                    viewModel.inputs.value?.dateSimpleFormat =
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
        //val myString = "some value" //the value you want the position for


        //val spinnerPosition = mySpinner.getAdapter().getPosition(myString)
        //set the default according to value

        //set the default according to value
        //yearSpinner.setSelection(spinnerPosition)


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



        binding.mortgageLoan.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    initialLoanAmount = s.toString().toDouble()
                    viewModel.inputs.value?.loanAmount = s.toString().toDouble()
                    //setting new var values to dataclass in ViewModel State.
                    getResults()
                }
            }
        })

        binding.mortgageInterest.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    interest = s.toString().toDouble()
                    viewModel.inputs.value?.interestAmount = s.toString().toDouble()
                    getResults()
                }
            }
        })

        binding.mortgageDown.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    downPayment = s.toString().toDouble()
                    viewModel.inputs.value?.downAmount = s.toString().toDouble()
                    getResults()
                }
            }
        })
    }

    fun getResults() {
        viewModel.calculate(AmortizationCalculator())
        scheduleRecycler?.adapter?.notifyDataSetChanged()
        setupObservers()
        setUpPieChart()
    }

    //setupObservers
    private fun setupObservers() {
        viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {
            bindResults(it!!.last())
        })
    }


    private fun bindResults(amortizationResults: AmortizationResults) {
        binding.monthlyPayment.text = ("$${amortizationResults.monthlyPayment}")
        binding.numberofPayments.text = viewModel.scheduleArrayList!!.size.toString()
        binding.payOffDate.text = amortizationResults.monthId
        binding.totalInterest.text = ("$${amortizationResults.totalInterest}")
        binding.totalAmount.text = ("$${String.format("%,.2f", amortizationResults.totalAmount)}")
        //Setting up todays date
        if (viewModel.inputs.value!!.dateSimpleFormat == "") {
            binding.startBtn.text = ("${month + 1}/$day/$year")
            viewModel.inputs.value!!.dateSimpleFormat = ("${month + 1}/$day/$year")
        } else {
            //Put today's or custom selected date in Model... ready to save.
            binding.startBtn.text = viewModel.inputs.value!!.dateSimpleFormat
        }
        setUpPieChart()
    }


    fun setUpPieChart() {
        val chart = binding.pieChart
        val transparentColor = Color.parseColor("#434343")
        val holeColor = Color.parseColor("#40F3F3F3")

        chart.setUsePercentValues(true)
        chart.isRotationEnabled = false
        chart.setTouchEnabled(false)
        chart.description.isEnabled
        chart.setExtraOffsets(0f, 0f, 0f, 0f)
        chart.setDrawEntryLabels(false)
        chart.isDrawHoleEnabled
        chart.setHoleColor(holeColor)
        chart.holeRadius = 60f
        chart.transparentCircleRadius = 65f
        chart.setTransparentCircleColor(transparentColor)
        chart.setTransparentCircleAlpha(9000)
        chart.description.text = ""



        val yValue = ArrayList<PieEntry>()

        yValue.add(PieEntry(viewModel.graphProgressPink().toFloat(), "Interest %"))
        yValue.add(PieEntry(viewModel.graphProgressBlue().toFloat(), "Principal %"))

        val blueColor = Color.parseColor("#61CFF8")
        val pinkColor = Color.parseColor("#FE657A")

        val dataSet = PieDataSet(yValue, "")

        dataSet.colors = mutableListOf<Int>(pinkColor, blueColor)

        val pieData = PieData(dataSet)
        pieData.setValueTextSize(10f)
        pieData.setValueTextColor(Color.YELLOW)

        chart.data = pieData // set data and notifyDataSetChange
        chart.invalidate() // refresh chart


    }


}


/*viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {})*/