package com.example.mortgagecalculator.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mortgagecalculator.MainActivity
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.model.MortgageDefaults
import com.example.mortgagecalculator.model.ScheduleOutput
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.schedule_fragment.*
import kotlin.math.absoluteValue
import kotlin.math.pow


class MainFragment : Fragment() {


    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.main_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = activity?.run { ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("invalid Activity")


        var initialLoanAmount :Double
        var years: Double
        var interest :Double
        var downPayment : Double


        //getting data class variables from ViewModel state.
        initialLoanAmount = viewModel.state.value!!.loanAmount
        val loanInput: String = initialLoanAmount.toString()
        mortgageLoan.setText(loanInput)

        years = viewModel.state.value!!.yearAmount
        val yearInput: String = years.toString()
        mortgageYears.setText(yearInput)

        interest = viewModel.state.value!!.interestAmount
        val interestInput: String = interest.toString()
        mortgageInterest.setText(interestInput)

        downPayment = viewModel.state.value!!.downAmount
        val downInput: String = downPayment.toString()
        mortgageDown.setText(downInput)



        fun quickMaths(): Double {
            val i = interest.div(100 * 12)

            val calExponent = (i.plus(1).let {
                years.times(12).let { it1 ->
                    it.pow(it1)
                }
            })
            return ((calExponent.let { i.times(it) }).div(calExponent - 1)).let {
                (downPayment.let { initialLoanAmount.minus(it) }).times(it)
            }
        }

        fun getResults() {
            val monthlyPayment = quickMaths()
            val firstInterestPayment = ((interest.div(100 * 12))*initialLoanAmount)
            val firstPrincipalPayment = monthlyPayment-firstInterestPayment
            val mortgageAfterPayment = initialLoanAmount-firstPrincipalPayment
            textView.text = monthlyPayment.toString()
            firstMonthlyInterest.text = firstInterestPayment.toString()
            firstMonthlyPrincipal.text = firstPrincipalPayment.toString()
            firstPaymentMortgage.text = mortgageAfterPayment.toString()
            //principal payment
            scheduleTest.text = ((interest.div(100 * 12))*mortgageAfterPayment).toString()
            viewModel.scheduleArrayList?.clear()
            scheduleRecycler?.adapter?.notifyDataSetChanged()
            var newAmount = initialLoanAmount
            var newPrincipal = firstPrincipalPayment
            var newInterestPayment = firstInterestPayment
            for (i in 1..12) {
                var interestPaymentHolder = newInterestPayment
                var mortgageHolder = newAmount
                var principalHolder = newPrincipal

                fun scheduleLoop(): Double {
                    var mortgage = mortgageHolder-principalHolder
                    newAmount = mortgage-(monthlyPayment-((interest.div(100 * 12))*mortgage))
                    newPrincipal = monthlyPayment-interestPaymentHolder
                    newInterestPayment = ((interest.div(100 * 12))*mortgageHolder)
                    return mortgage-(monthlyPayment-((interest.div(100 * 12))*mortgage))
                        }
                viewModel.scheduleArrayList?.add(ScheduleOutput(i.toString(), scheduleLoop().toString(), interestPaymentHolder.toString(), principalHolder.toInt().toString()))

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
                    //setting new var values to dataclass in ViewModel State.
                    viewModel.state.value?.loanAmount = s.toString().toDouble()
                    getResults()

                }
            }
        })

        mortgageYears.addTextChangedListener(object : TextWatcher {

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
                    years = s.toString().toDouble()
                    viewModel.state.value?.yearAmount = s.toString().toDouble()
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
                //viewModel.saveName(s.toString().toDouble())
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
                    downPayment = s.toString().toDouble()
                    viewModel.state.value?.downAmount = s.toString().toDouble()
                    getResults()
                }
            }
        })

        getResults()
    }

}


/*viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {})*/

