package com.example.mortgagecalculator.ui.main.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mortgagecalculator.db.Input
import com.example.mortgagecalculator.model.AmortizationCalculator
import com.example.mortgagecalculator.model.AmortizationResults
import com.example.mortgagecalculator.repositories.InputRepository
import com.example.mortgagecalculator.utils.ExtraPayments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class AmortizationViewModel @ViewModelInject constructor(
    private val repository: InputRepository
) : ViewModel() {

    //create the job, which implements coroutines context.
    var job = Job()

    //create the coroutine context with the job and the dispatcher(identifies the Thread that will be used)
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    private val _inputs = MutableLiveData<Input>()

    val inputs: MutableLiveData<Input> get() = _inputs

    var paymentArrayList: ArrayList<String> = arrayListOf()

    var scheduleArrayList: ArrayList<AmortizationResults>? = null

    val scheduleLiveData: MutableLiveData<ArrayList<AmortizationResults>?> = MutableLiveData()




    fun getCalculationResults() {
        scope.launch {
            scheduleArrayList = inputs.value?.let { AmortizationCalculator.calculate(it) }
            scheduleLiveData.postValue(scheduleArrayList)
        }
    }

    fun recurringPayments() = inputs.value?.let { ExtraPayments.recurringPayments(ArrayList(), it) }

    fun getPieChartProgress() =
        inputs.value?.let { AmortizationCalculator.calculatePieProgress(it) }


    fun getProgress(position: String, total: String) =
        ((position.replace(Regex(","), "").toDouble()) / (total.replace(Regex(","), "")
            .toDouble())) * 100


    fun getExtraPaymentSize(): Int {
        if (!scheduleArrayList.isNullOrEmpty()) {
            for (i in 0 until scheduleArrayList!!.size) {
                if (scheduleArrayList!![i].additionalPayment == "EP") {
                    paymentArrayList.add(i.toString())
                }
            }
        }
        val numberOfPayments:Int = paymentArrayList.size
        paymentArrayList.clear()
        return numberOfPayments
    }

    suspend fun deleteInput(input: Input): Any {
        return repository.deleteInput(input)
    }

    suspend fun insertInputs(input: Input): Any {
        return repository.insertInputs(input)
    }

    suspend fun getInputs(): List<Input> {
        return repository.getAllInputs()
    }

    suspend fun databaseSize(): Int {
        return repository.numberOfItemsInDB()
    }

    init {
        _inputs.value = Input()
    }


}


