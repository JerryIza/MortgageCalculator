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
import androidx.lifecycle.ViewModelProvider
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.model.ScheduleOutput
import com.example.mortgagecalculator.ui.main.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.schedule_fragment.*
import java.text.DecimalFormat
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
        mortgageDown.setText( fullDecimalFormat.format(totalPaid))


        fun monthlyPayment(): Double {
            val i = interest.div(100 * 12)
            val calExponent = (i.plus(1).let {
                years.times(12).let { it1 ->
                    it.pow(it1)
                }
            })
            return ((calExponent.let { i.times(it) }).div(calExponent - 1)).let { it ->
                (downPayment.let { initialLoanAmount.minus(it) }).times(it)
            }
        }
        println(monthlyPayment())

        fun getAmortization() {
            viewModel.scheduleArrayList?.clear()
            scheduleRecycler?.adapter?.notifyDataSetChanged()

            val monthlyPayment = monthlyPayment()
            val numberQuotas = (years*12).toInt()
            var newInterestPayment = ((interest.div(100 * 12))*(initialLoanAmount-downPayment))
            var newPrincipal = monthlyPayment-newInterestPayment
            var newMortgage = (initialLoanAmount-downPayment)-newPrincipal
            var paidInterest  = newInterestPayment


            for (i in 1..numberQuotas) {
                viewModel.scheduleArrayList?.add(
                    ScheduleOutput((i).toString(),
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
            val totalPaid = (formattedInterest.toDouble()+ initialLoanAmount)

            numberofPayments.text = numberQuotas.toString()
            moPayment.text = String.format("%,.2f", monthlyPayment)

            totalAmount.text = String.format("%,.2f", totalPaid)
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
                getAmortization()

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
                    getAmortization()
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
                    getAmortization()

                }
            }
        })

        mortgageDown.addTextChangedListener(object : TextWatcher {


            private var fullDecimalFormat = DecimalFormat("#,###.######")
            private var normalFormat = DecimalFormat("#,###")
            private var decimalOnlyFormat = DecimalFormat(".######")
            private var hasFractionalPart: Boolean
            private var testFractionBool: Boolean

            private var deleteKeyPressed : Boolean


            override fun afterTextChanged(s: Editable) {

                val offsetPos: Int = mortgageDown.text.length
                mortgageDown.removeTextChangedListener(this)


                val textInput = s.toString().replace(Regex("[$,]"), "")
                //hasFractionalPart = s.toString().contains(fullDecimalFormat.decimalFormatSymbols.decimalSeparator.toString())
                hasFractionalPart = s.toString().contains(".")



                val offsetSelectedPos : Int = mortgageDown.selectionStart
                //add a zero in front of "."
                if (textInput  == ".") {
                    mortgageDown.setText( fullDecimalFormat.format(0.toDouble()))
                }
                //full decimal
                else if (hasFractionalPart && !textInput[0].toString().contains(".")) {
                    mortgageDown.setText( fullDecimalFormat.format(textInput.toDouble()))
                    println("wtf")
                }
                //decimalonly format
                else if (hasFractionalPart && (textInput[0].toString().contains("."))){
                    mortgageDown.setText( decimalOnlyFormat.format(textInput.toDouble()))
                    println("wtf2")

                }
                //No decimals
                else if (s.isNotEmpty()) {
                    mortgageDown.setText(normalFormat.format(textInput.toDouble()))
                }
                else {
                    //save zero
                    viewModel.state.value?.downAmount =  0.toDouble()
                }

                val maxPos: Int = mortgageDown.text.length
                //offsetting by one where there is a comma.
                val selectedPos = offsetSelectedPos + (maxPos - offsetPos)
                //offset has to be true, as well as delete soft key
                 if (selectedPos != maxPos && selectedPos > offsetSelectedPos && deleteKeyPressed && textInput != ".") {
                    println("first")
                    //getting string with characters included
                    val fullInput = (fullDecimalFormat.format(textInput.toDouble()))
                    println(fullInput)
                    //before cursor text
                    val before = fullInput.substring(0, offsetSelectedPos)
                    println(before)
                    //after cursor text
                    val after = fullInput.substring(offsetSelectedPos)
                    println(after)
                    //before cursor range minus the ending value.
                    val newBefore = before.substring(0, before.length - 1)
                    println(newBefore)
                    // "gluing" the text back together after getting rid of the value deleted
                    val outputNumber = newBefore + after
                    val finalNumber = outputNumber.replace(Regex("[$,]"), "")

                     if (hasFractionalPart){
                        mortgageDown.setText(fullDecimalFormat.format(finalNumber.toDouble()))}
                    else {
                        mortgageDown.setText(normalFormat.format(finalNumber.toDouble()))
                    }
                     val newFormatLength = mortgageDown.text.length

                     //when splicing and getting rid of a comma, we need to offset by 2
                     if (newFormatLength == maxPos -2 && selectedPos != 0) {
                         mortgageDown.setSelection(selectedPos - 2)
                     }
                     else {
                         mortgageDown.setSelection(selectedPos - 1)
                     }
                     deleteKeyPressed = false
                }
                //placing cursor on initial selected position
                else if (selectedPos in 1..maxPos) {
                    mortgageDown.setSelection(selectedPos)
                     println("tehe")
                }
                //placing cursor at zero when empty
                else {
                    mortgageDown.setSelection(offsetSelectedPos)
                     println("tehe")

                }
                mortgageDown.addTextChangedListener(this)
                getAmortization()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
                if (after < count) {
                    deleteKeyPressed = true
                }
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (s.isNotEmpty() && s.toString() != ".") {
                    val textInput = s.toString().replace(Regex("[,]"), "")
                    downPayment = textInput.toDouble()
                    viewModel.state.value?.downAmount = textInput.toDouble()
                }
                else
                {
                    downPayment = 0.toDouble()
                }
            }

            init {
                println("Init")
                fullDecimalFormat.isDecimalSeparatorAlwaysShown = true
                hasFractionalPart = false
                testFractionBool = false
                deleteKeyPressed = false

            }

        })

    }
 }

/*viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {})*/