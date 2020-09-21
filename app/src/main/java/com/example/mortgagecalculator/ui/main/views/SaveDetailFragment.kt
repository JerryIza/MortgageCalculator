package com.example.mortgagecalculator.ui.main.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.databinding.DetailSaveFragmentBinding
import com.example.mortgagecalculator.db.Input
import com.example.mortgagecalculator.ui.main.viewmodels.AmortizationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaveDetailFragment : Fragment() {

    private lateinit var binding: DetailSaveFragmentBinding

    private val viewModel: AmortizationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailSaveFragmentBinding.inflate(inflater, container, false)
        setupObserver()

        val navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)


        var title = viewModel.inputs.value!!.savedInputsTitle
        var loan = viewModel.inputs.value!!.loanAmount
        var interest = viewModel.inputs.value!!.interestAmount
        var years = viewModel.inputs.value!!.yearAmount
        var spinnerPos = viewModel.inputs.value!!.yearSpinnerPos
        var down = viewModel.inputs.value!!.downAmount
        //set default spinner pos, rather than starting 0 pos
        viewModel.inputs.value?.yearSpinnerPos?.let { binding.yearSpinner.setSelection(it) }



        binding.dbTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    title = s.toString()
                }
            }
        })

        binding.dbLoan.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    loan = s.toString().toDouble()
                }
            }
        })
        binding.dbInterest.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    interest = s.toString().toDouble()
                }
            }
        })

        binding.dbDown.addTextChangedListener(object : TextWatcher {
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
                    down = s.toString().toDouble()
                }
            }
        })
        binding.yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val s: Int = parent?.getItemAtPosition(position).toString().toInt()
                spinnerPos = position
                years = s
            }
        }

        if (viewModel.inputs.value!!.payments.isNotEmpty()) {
            binding.paymentsSwitch.isChecked = true
        }
        binding.saveBtn.setOnClickListener {
            if (binding.dbTitle.text.isNullOrEmpty()) {
                binding.titleTextInput.error = "Title cannot be empty."
            } else if (binding.paymentsSwitch.isChecked && viewModel.inputs.value!!.payments.isNotEmpty()) {
                println("with payments executed")
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.insertInputs(
                        Input(
                            savedInputsTitle = title,
                            loanAmount = loan,
                            interestAmount = interest,
                            yearAmount = years,
                            downAmount = down,
                            yearSpinnerPos = spinnerPos,
                            startDateFormat = viewModel.inputs.value!!.startDateFormat,
                            payments = viewModel.inputs.value!!.payments,
                            extraPaymentSize = viewModel.inputs.value!!.extraPaymentSize,
                            modifiedAt = System.currentTimeMillis()
                        )
                    )
                    navController.navigate(R.id.action_saveDetailFragment_to_saveFragment)
                }
            } else if (!binding.paymentsSwitch.isChecked || viewModel.inputs.value!!.payments.isEmpty()) {
                GlobalScope.launch(Dispatchers.Main) {
                    println("NOT INCLUDED")

                    viewModel.insertInputs(
                        Input(
                            savedInputsTitle = title,
                            loanAmount = loan,
                            interestAmount = interest,
                            yearAmount = years,
                            downAmount = down,
                            yearSpinnerPos = spinnerPos,
                            startDateFormat = viewModel.inputs.value!!.startDateFormat,
                            modifiedAt = System.currentTimeMillis()

                        )
                    )
                    navController.navigate(R.id.action_saveDetailFragment_to_saveFragment)
                }
            }
        }
        return binding.root
    }


    //Observe the current values on text
    private fun setupObserver() {
        viewModel.inputs.observe(viewLifecycleOwner, Observer {
            binding.dbTitle.setText(it.savedInputsTitle)
            binding.dbLoan.setText(it.loanAmount.toString())
            binding.dbInterest.setText(it.interestAmount.toString())
            //binding.dbYears.setText(it.yearAmount.toString())
            binding.dbDown.setText(it.downAmount.toString())
            binding.dbDate.text = ("Start Date: ${it.startDateFormat}")
            binding.paymentsView.text = ("# of Payments: ${viewModel.inputs.value!!.extraPaymentSize}")

        })
    }
}