package com.example.mortgagecalculator.ui.main

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

             override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

}
