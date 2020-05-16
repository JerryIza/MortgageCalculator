package com.example.mortgagecalculator.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mortgagecalculator.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.math.pow


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }



    private lateinit var viewModel: MainViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = MainFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        val MyInput = MortgageInput(
            "10000","5","30","1")

        val initialPrincipal = MyInput.inputLoan.toDouble()
        val loanYears = MyInput.inputYears.toDouble()
        val interest = MyInput.inputInterest.toDouble()
        val downPayment = MyInput.inputDownPayment.toDouble()


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
                (downPayment.let { initialPrincipal.minus(it) }).times(
                    it
                )
            }

        }

        val tehe = quickMaths()

        binding.btn.setOnClickListener{
            binding.textView.text = tehe.toString()

        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}


