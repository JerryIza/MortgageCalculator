package com.example.mortgagecalculator.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mortgagecalculator.R
import kotlinx.android.synthetic.main.main_fragment.*
import java.lang.Exception
import kotlin.math.pow



class MainFragment : Fragment() {


    private lateinit var viewModel: MainViewModel


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = activity?.run {  ViewModelProvider(this).get(MainViewModel::class.java)
        }?: throw Exception("invalid Activity")


        var initialLoanAmount = 0.toDouble()
        var years = 0.toDouble()
        var interest = 0.toDouble()
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


        btn.setOnClickListener{
            textView.text = quickMaths().toString()

        }


        navBtn.setOnClickListener{

            view.findNavController()
                .navigate(R.id.action_mainFragment_to_scheduleFragment3)

        }

        loanAmount.addTextChangedListener (object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.isNotEmpty()){
                initialLoanAmount = s.toString().toDouble()}


            }
        })

        loanYears.addTextChangedListener (object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.isNotEmpty()){
                years = s.toString().toDouble().times(12)}
            }
        })

        interestAmount.addTextChangedListener (object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.isNotEmpty()){
                interest = s.toString().toDouble().div((100 * 12))}
            }
        })
        moneyDown.addTextChangedListener (object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.isNotEmpty()){
                downPayment = s.toString().toDouble()}
            }
        })
        //
    }
}


