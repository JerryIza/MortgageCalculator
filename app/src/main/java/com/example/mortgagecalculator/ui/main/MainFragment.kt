package com.example.mortgagecalculator.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
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
import java.text.DecimalFormat
import kotlin.math.pow


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
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
        val downInput: String = String.format("%,d", wtf.toInt())
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


            private var fullDecimalFormat = DecimalFormat("#,###.######")
            private var normalFormat = DecimalFormat("#,###")
            private var decimalOnlyFormat = DecimalFormat(".######")
            private var hasFractionalPart: Boolean






            override fun afterTextChanged(s: Editable) {
                println("afterTextChanged text changed")
                println("1")
                val offsetPos: Int = mortgageDown.text.length
                mortgageDown.removeTextChangedListener(this)
                println("2")


                val textInput = s.toString().replace(Regex("[$,]"), "")
                println("3")

                hasFractionalPart = s.toString().contains(fullDecimalFormat.decimalFormatSymbols.decimalSeparator.toString())
                val offsetSelectedPos : Int = mortgageDown.selectionStart
                println("4")


                when {
                    hasFractionalPart -> {
                        //overloaded call use null to prevent crash
                        println("has decimal")
                        mortgageDown.setText( fullDecimalFormat.format(textInput.toDouble()), null)
                    }
                    s.isNotEmpty() -> {
                        println("no decimal")
                        mortgageDown.setText(  normalFormat.format(textInput.toDouble()), null)
                    }
                    else -> {
                        println("empty string")
                        downPayment =  0.toDouble()
                    }
                }
                println("5")
                val maxPos: Int = mortgageDown.text.length
                val selectedPos = offsetSelectedPos + (maxPos - offsetPos)

                //placing cursor at comma or decimal
                if (selectedPos != maxPos && selectedPos > offsetSelectedPos) {
                    println("5.2")

                    //getting string with characters included
                    val fullInput = (normalFormat.format(textInput.toDouble()))
                    //before cursor text
                    val before = fullInput.substring(0, offsetSelectedPos)
                    //after cursor text
                    val after = fullInput.substring(offsetSelectedPos)
                        //before cursor range minus the ending value.
                    val newBefore = before.substring(0, before.length - 1)
                    // "gluing" the text back together after getting rid of the value deleted
                    val outputNumber = newBefore + after
                    val finalNumber = outputNumber.replace(Regex("[$,]"), "")
                    println("5.5")
                    if (hasFractionalPart){
                        mortgageDown.setText(fullDecimalFormat.format(finalNumber.toDouble()))}
                    else {
                        mortgageDown.setText(normalFormat.format(finalNumber.toDouble()))
                    }
                    //cursors after delete may have make to customs.
                    mortgageDown.setSelection(selectedPos-1)
                    println("first")
                }
                //placing cursor at end of numbers
                else if (selectedPos in 1..maxPos) {
                    mortgageDown.setSelection(selectedPos)
                    println("second")

                } else {
                 // placing cursor at next available number that is not zero.
                    mortgageDown.setSelection(offsetSelectedPos)
                    println("third")

                }
                //getResults()

                mortgageDown.addTextChangedListener(this)

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
                println("before text changed")
            }
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {

                println("onTextChanged text changed")
                if (s.isNotEmpty()) {
                    val textInput = s.toString().replace(Regex("[$,.]"), "")
                    if (textInput.isNotEmpty()){
                    downPayment = textInput.toDouble()
                    viewModel.state.value?.downAmount = textInput.toDouble()
                    }
                }

            }

            init {
                println("Init")
                fullDecimalFormat.isDecimalSeparatorAlwaysShown = true
                hasFractionalPart = false
            }



        })



    }

}

/*fun afterTextChanged(s: Editable) {
    mortgageStart.removeTextChangedListener(this)

    MortgageStart.addTextChangedListener(this)
}

*/



/*viewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {})*/




