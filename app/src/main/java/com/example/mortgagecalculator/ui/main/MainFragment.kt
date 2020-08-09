package com.example.mortgagecalculator.ui.main

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
import androidx.lifecycle.ViewModelProvider
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.model.ScheduleOutput
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.schedule_fragment.*
import kotlin.math.pow


class MainFragment : Fragment()  {


    private lateinit var viewModel: MainViewModel

    lateinit var option : Spinner
    lateinit var result : TextView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.main_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = activity?.run { ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("invalid Activity")


        var initialLoanAmount : Double
        var years: Double
        var interest : Double
        var downPayment : Double


        //getting data class variables from ViewModel state.
        initialLoanAmount = viewModel.state.value!!.loanAmount
        val loanInput: String = initialLoanAmount.toString()
        mortgageLoan.setText(loanInput)
        //  these and below are reformatted
        years = viewModel.state.value!!.yearAmount

        interest = viewModel.state.value!!.interestAmount
        val interestInput: String = String.format("%,.2f", interest)
        mortgageInterest.setText(interestInput)

        downPayment = viewModel.state.value!!.downAmount
        val wtf = downPayment
        val downInput: String = String.format("%,.2f", wtf)
        mortgageDown.setText(downInput)


        button.setOnClickListener {
            downPayment = viewModel.state.value!!.downAmount
            val motherbitch = downPayment
            mortgageDown.setText(String.format("%,.2f", motherbitch))
        }



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
            val firstInterestPayment = ((interest.div(100 * 12))*(initialLoanAmount-downPayment))
            val firstPrincipalPayment = monthlyPayment-firstInterestPayment
            val mortgageAfterPayment = (initialLoanAmount-downPayment)-firstPrincipalPayment
            val numberQuotas = (years*12).toInt()
            moPayment.text = String.format("%,.2f", monthlyPayment)
            numberofPayments.text = numberQuotas.toString()

            viewModel.scheduleArrayList?.clear()
            scheduleRecycler?.adapter?.notifyDataSetChanged()


            var newInterestPayment = firstInterestPayment
            var newPrincipal = firstPrincipalPayment
            var newMortgage = mortgageAfterPayment
            var paidInterest  = firstInterestPayment



            for (i in 1..numberQuotas) {
                //we have to move the scheduloutput for cleaner code
                viewModel.scheduleArrayList?.add(ScheduleOutput((i).toString(),
                    String.format("%,.2f", newMortgage),
                    String.format("%,.2f", newInterestPayment),
                    String.format("%,.2f", newPrincipal),
                    String.format("%,.2f", paidInterest)
                ))
                //updating vars happens everytime the loop is run
                newInterestPayment = ((interest.div(100 * 12))*newMortgage)
                newPrincipal = monthlyPayment-newInterestPayment
                newMortgage -= newPrincipal
                paidInterest += newInterestPayment
            }

            //need to remove "," other wise parsing will fail
            val lastInterestPosition = (viewModel.scheduleArrayList!!.last().totalInterest)
            val formattedInterest = lastInterestPosition.replace(Regex(","), "")
            val wtf = (formattedInterest.toDouble()+ initialLoanAmount)

            totalAmount.text = String.format("%,.2f", wtf)
            totalInterest.text = lastInterestPosition
        }

        option = yearSpinner
        result = spinnerresults

        val options = arrayOf("30","15")

        option.adapter = context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, options) }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                result.text = "Select an option "
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                result.text = options[position]
                val s = options[position]
                years = s.toDouble()
                viewModel.state.value?.yearAmount = s.toDouble()
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
                    getResults()
                    //setting new var values to dataclass in ViewModel State.

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
                val fukubitch = viewModel.state.value!!.downAmount
                println("3")
                val motherbitch = fukubitch
                println("4")
                mortgageDown.removeTextChangedListener(this)
                //as soon as format changes it goes to infinite loop, if it equal to text with add ,

                mortgageDown.setText("$" + (String.format("%,.2f", motherbitch)))
                mortgageDown.setSelection(mortgageDown.text.toString().length-3)
                println("5")
                mortgageDown.addTextChangedListener(this)
                println("6")


            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
                println("before text changed")
            }
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    println("1")

                    //replace two values at once
                    var textInput = s.toString().replace(",", "")
                    textInput = textInput.replace("$", "")
                    downPayment = textInput.toDouble()
                    viewModel.state.value?.downAmount = textInput.toDouble()
                    getResults()
                    println("2")

                }
            }
        })

    }

}


/*viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {})*/

