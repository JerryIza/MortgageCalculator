package com.example.mortgagecalculator.ui.main.views

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
import com.example.mortgagecalculator.databinding.MainFragmentBinding
import com.example.mortgagecalculator.model.AmortizationCalculator
import com.example.mortgagecalculator.model.AmortizationResults
import com.example.mortgagecalculator.model.ExtraPayments
import com.example.mortgagecalculator.ui.main.viewmodels.AmortizationViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.schedule_fragment.*
import java.text.DecimalFormat


class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    private lateinit var viewModel: AmortizationViewModel

    lateinit var option: Spinner
    lateinit var result: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
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
                }
            }
        })
    }

    fun getResults() {

        viewModel.calculateTest(AmortizationCalculator())
        scheduleRecycler?.adapter?.notifyDataSetChanged()
        println("GetResults")
        setupObservers()
    }

    //setupObservers
    private fun setupObservers() {
        println("observer called")
        viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {
            bindResults(it!!.last())
        })

    }

    private fun bindResults(amortizationResults: AmortizationResults) {
        binding.totalInterest.text = amortizationResults.totalInterest
        binding.totalAmount.text = amortizationResults.totalAmount
        binding.numberofPayments.text = amortizationResults.quotas
        binding.moPayment.text = amortizationResults.monthlyPayment
        binding.deleteView.text = viewModel.extraPaymentsLiveData.toString()

    }
}

/*viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {})*/