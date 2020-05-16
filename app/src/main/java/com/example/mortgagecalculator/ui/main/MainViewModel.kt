package com.example.mortgagecalculator.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    fun onTextChanged(
        s: CharSequence,
        start: String,
        before: String,
        count: String
    ) {
        Log.w("tag", "onTextChanged $s")
    }

}
