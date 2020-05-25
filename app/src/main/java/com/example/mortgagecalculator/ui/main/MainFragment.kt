package com.example.mortgagecalculator.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.math.pow


class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.main_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        var initialLoanAmount = 0.0

        val loanYears = loanYears.text.toString().toDouble()
        val interest = interestAmount.text.toString().toDouble()
        val downPayment = downPayment.text.toString().toDouble()

        loanAmount.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
               textView.setText(s)
            }
        })




        fun quickMaths(): Double {

            val nOfPayments = loanYears.times(12)
            val calculatedInt = interest.div((100 * 12))
            val calExponent = (calculatedInt.plus(1).let {
                nOfPayments.let { it1 ->
                    it.pow(it1)
                }
            })

            return ((calExponent.let {
                calculatedInt.times(
                    it
                )
            }).div(calExponent - 1)).let {
                (downPayment.let { initialLoanAmount.minus(it) }).times(
                    it
                )
            }

        }

        val tehe = quickMaths()

        btn.setOnClickListener{
            textView.text = tehe.toString()

        }




        navBtn.setOnClickListener{
            view?.findNavController()
                ?.navigate(R.id.action_mainFragment_to_scheduleFragment3)
        }

    }

}


