package com.example.mortgagecalculator.ui.main.views

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.databinding.ScheduleFragmentBinding
import com.example.mortgagecalculator.ui.main.adapters.ScheduleAdapter
import com.example.mortgagecalculator.ui.main.viewmodels.AmortizationViewModel
import kotlinx.android.synthetic.main.accept_dialog.*
import kotlinx.android.synthetic.main.payment_dialog.*


class ScheduleFragment : Fragment() {

    private lateinit var binding: ScheduleFragmentBinding

    private lateinit var viewModel: AmortizationViewModel

    private lateinit var adapter: ScheduleAdapter

    val dateArrayList: ArrayList<String>? = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ScheduleFragmentBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this).get(AmortizationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        setupRecyclerView()

        binding.recyclerViewOptions.setOnClickListener {
            showPopup(binding.recyclerViewOptions)
        }

    }

    private fun setupRecyclerView() {
        val navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)
        val scheduleList = viewModel.scheduleArrayList
        adapter = ScheduleAdapter(scheduleList!!) {
            viewModel.inputs.value?.epPosition = it
            navController.navigate(R.id.action_scheduleFragment_to_detailFragment)
        }
        binding.scheduleRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.scheduleRecycler.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(context, view)
        popup.inflate(R.menu.payment_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.recurringPayments -> {
                    paymentDialog()
                }
                R.id.deletePayments -> {
                    acceptDialog()
                }
            }
            true
        })
        popup.show()

    }

    private fun paymentDialog() {

        val dialog = activity?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.payment_dialog)
        //dialog.text_dialog.text = ("Do wish to load ${adapter.getItem(position).savedInputsTitle} Inputs?")

        val confirm = dialog.confirmBtn
        val cancel = dialog.cancelBtn
        val editText = dialog.additionalPayment
        val textInput = dialog.text_input_payments
        val datePicker = dialog.dpDate
        dateArrayList?.clear()

        dialog.show()
        confirm.setOnClickListener {
            if (editText.text.isNullOrEmpty()) {
                textInput.error = "Recurring payment cannot be empty."
            } else {
                viewModel.inputs.value?.endDateFormat = "" + (datePicker.month + 1) + "/" + datePicker.dayOfMonth + "/" + datePicker.year
                viewModel.recurringPayments(dateArrayList)
                for (i in (dateArrayList!!.size - 1) until (viewModel.scheduleArrayList!!.size)) {
                    viewModel.inputs.value?.payments?.set(
                        i.toString(), (editText.text.toString().toInt())
                    )
                }
                viewModel.getCalculationResults()
                adapter.notifyDataSetChanged()
                viewModel.updateExtraPaymentSize()
                dialog.dismiss()
            }
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun acceptDialog() {
        val dialog = activity?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.accept_dialog)
        val navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)
        val confirm = dialog.btn_yes
        val cancel = dialog.btn_no
        val text = dialog.text_dialog
        dialog.show()
        if (viewModel.inputs.value?.payments!!.isEmpty()) {
            text.text = ("There are no payments to delete.")
        } else {
            text.text = ("Do you wish to delete all ${(viewModel.inputs.value!!.extraPaymentSize)} extra payments? ")
        }
        confirm.setOnClickListener {
            viewModel.inputs.value!!.payments.clear()
            viewModel.getCalculationResults()
            adapter.notifyDataSetChanged()
            dialog.dismiss()

        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }


    }


}







