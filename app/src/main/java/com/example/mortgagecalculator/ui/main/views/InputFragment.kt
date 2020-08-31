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
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
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

    private lateinit var viewModel: AmortizationViewModel

    lateinit var option: Spinner
    lateinit var result: TextView

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


        var initialLoanAmount: Double
        var years: Int
        var interest: Double
        var downPayment: Double

        val fullDecimalFormat = DecimalFormat("#,###.######")


        //getting data class variables from ViewModel state, and formatting once ran once
        initialLoanAmount = viewModel.state.value!!.loanAmount
        val loanInput: String = initialLoanAmount.toString()
        mortgageLoan.setText(loanInput)
        //  these and below are reformatted
        years = viewModel.state.value!!.yearAmount

        interest = viewModel.state.value!!.interestAmount
        val interestInput: String = String.format("%,.2f", interest)
        mortgageInterest.setText(interestInput)

        downPayment = viewModel.state.value!!.downAmount
        val totalPaid = downPayment
        mortgageDown.setText(fullDecimalFormat.format(totalPaid))


        val textDate = binding.startBtn



        textDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val year = cal.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, mYear, mMonth, mDay ->
                    textDate.text = "" + (mMonth + 1) + "/" + mDay + "/" + mYear
                    //both are instances not raw ints
                    viewModel.state.value?.dateSimpleFormat =
                        "" + (mMonth + 1) + "/" + mDay + "/" + mYear
                },
                //where dialogstarts maybe do an if, so we don't forge
                year,
                month,
                day
            )
            datePickerDialog.show()
            datePickerDialog.setOnDismissListener {
                getResults()
            }
        }

        option = yearSpinner
        result = spinnerresults

        val options = arrayOf("30", "15")

        option.adapter =
            context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, options) }

        binding.yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                result.text = "Select an option "
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                result.text = options[position]
                val s = options[position]
                years = s.toInt()
                viewModel.state.value?.yearAmount = s.toInt()
                getResults()
                setUpPieChart()
            }
        }

        mortgageLoan.addTextChangedListener(object : TextWatcher {
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
                    viewModel.state.value?.loanAmount = s.toString().toDouble()
                    //setting new var values to dataclass in ViewModel State.
                    getResults()
                    setUpPieChart()
                }
            }
        })

        mortgageInterest.addTextChangedListener(object : TextWatcher {
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
                    viewModel.state.value?.interestAmount = s.toString().toDouble()
                    getResults()
                    setUpPieChart()
                }
            }
        })

        mortgageDown.addTextChangedListener(object : TextWatcher {
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
                    viewModel.state.value?.downAmount = s.toString().toDouble()
                    getResults()
                    setUpPieChart()
                }
            }
        })

    }

    fun getResults() {
        println("get results executed")
        viewModel.Calculate(AmortizationCalculator())
        scheduleRecycler?.adapter?.notifyDataSetChanged()
        println("GetResults")
        setupObservers()
        setUpPieChart()


    }

    //setupObservers
    private fun setupObservers() {
        println("observer called")
        viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {
            bindResults(it!!.last())
        })

    }

    private fun bindResults(amortizationResults: AmortizationResults) {
        println("bindresultsExecute")
        binding.totalInterest.text = amortizationResults.totalInterest
        binding.monthlyPayment.text = amortizationResults.monthlyPayment
        binding.payOffDate.text = amortizationResults.monthId
        binding.totalAmount.text = String.format("%,.2f", amortizationResults.totalAmount)
        binding.numberofPayments.text = viewModel.scheduleArrayList!!.size.toString()

        setUpPieChart()
    }


    fun setUpPieChart() {
        val chart = binding.pieChart

        //move to own function
        chart.setUsePercentValues(true)
        chart.isRotationEnabled = false
        chart.setTouchEnabled(false)
        chart.description.isEnabled
        chart.setExtraOffsets(0f, 5f, 0f, -10f)

        val transparentColor = Color.parseColor("#434343")
        chart.isDrawHoleEnabled
        chart.setHoleColor(Color.WHITE)
        chart.holeRadius = 60f
        chart.transparentCircleRadius = 65f
        chart.setTransparentCircleColor(transparentColor)
        chart.setTransparentCircleAlpha(9000)

        val yValue = ArrayList<PieEntry>()

        yValue.add(PieEntry(viewModel.graphProgressPink().toFloat(), "Interest"))
        yValue.add(PieEntry(viewModel.graphProgressBlue().toFloat(), "Principal"))

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