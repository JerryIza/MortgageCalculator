package com.example.mortgagecalculator.ui.main.views

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.net.ParseException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.databinding.SaveFragmentBinding
import com.example.mortgagecalculator.ui.main.viewmodels.CalculatorViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

//R.layout.save_fragment will get deleted
@Suppress("DEPRECATED_IDENTITY_EQUALS")
@AndroidEntryPoint
class SaveFragment : Fragment(R.layout.save_fragment) {

    private lateinit var binding: SaveFragmentBinding

    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SaveFragmentBinding.inflate(inflater, container, false)



        return binding.root
    }



}








