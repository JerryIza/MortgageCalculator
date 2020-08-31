package com.example.mortgagecalculator.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.navHostFragment))


        findNavController(R.id.navHostFragment).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment, R.id.scheduleFragment, R.id.saveFragment ->
                    bottomNavigationView.visibility = View.VISIBLE
                else -> bottomNavigationView.visibility = View.GONE
            }
        }
    }
}

