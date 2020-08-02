package com.example.mortgagecalculator.ui.main

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.model.MortgageDefaults
import com.example.mortgagecalculator.model.ScheduleOutput
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.schedule_fragment.*
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


        var tehe = MortgageDefaults(10000.00, 30.00 , 5.0)

        var initialLoanAmount = 100000.00
        var years = tehe.yearAmount.times(12)
        var interest = tehe.interestAmount.div(100 * 12)
        var downPayment = 0.toDouble()


        fun quickMaths(): Double {

            val calExponent = (interest.plus(1).let {
                years.let { it1 ->
                    it.pow(it1)
                }
            })
            return ((calExponent.let { interest.times(it) }).div(calExponent - 1)).let {
                (downPayment.let { initialLoanAmount.minus(it) }).times(it)
            }
        }


        fun getResults() {
            textView.text = quickMaths().toString()
            viewModel.scheduleArrayList?.clear()
            scheduleRecycler?.adapter?.notifyDataSetChanged()
            for (i in 1..50) {
                viewModel.scheduleArrayList?.add(ScheduleOutput(i.toString(), i.toString()))
            }


        }


        loanAmount.addTextChangedListener(object : TextWatcher {
            //a crash is caused everytime value goes to zero
            override fun afterTextChanged(s: Editable) {
                getResults()
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
                    getResults()

                }
            }
        })

        interestAmount.addTextChangedListener(object : TextWatcher {

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
                    interest = s.toString().toDouble().div((100 * 12))
                    getResults()

                }
            }
        })

        loanYears.addTextChangedListener(object : TextWatcher {

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
                    years = s.toString().toDouble().times(12)
                    getResults()

                }
            }
        })


        moneyDown.addTextChangedListener(object : TextWatcher {

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
                    getResults()
                }
            }
        })
        getResults()
    }

}


/*viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {})*/

