package com.example.mortgagecalculator.ui.main.views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.databinding.SaveFragmentBinding
import com.example.mortgagecalculator.db.Input
import com.example.mortgagecalculator.model.AmortizationCalculator
import com.example.mortgagecalculator.ui.main.adapters.SaveAdapter
import com.example.mortgagecalculator.ui.main.viewmodels.AmortizationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.save_options_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


//R.layout.save_fragment will get deleted
@AndroidEntryPoint
class SaveFragment : Fragment() {

    private lateinit var binding: SaveFragmentBinding

    //by activityViewModels creates view model scope for the whole activity. vs by viewmodels = individual fragment
    private val viewModel: AmortizationViewModel by activityViewModels()

    private lateinit var adapter: SaveAdapter

    private lateinit var dummyInputs: ArrayList<Input>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)
        setupObservers()
        binding = SaveFragmentBinding.inflate(inflater, container, false)


        binding.floatingSaveButton.setOnClickListener {
            viewModel.inputs.value?.savedInputsTitle = ""
            navController.navigate(R.id.action_saveFragment_to_saveDetailFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        updateList()
    }

    private fun setupRecyclerView() {
        dummyInputs = arrayListOf(Input())
        adapter = SaveAdapter(requireContext(), dummyInputs) { showDialog(it) }
        binding.saveRv.layoutManager = LinearLayoutManager(requireContext())
        binding.saveRv.adapter = adapter
        adapter.notifyDataSetChanged()
    }
    private fun updateList(){
        GlobalScope.launch(Dispatchers.Main){
            adapter.setItems(ArrayList(viewModel.getInputs()))
        }
    }

    private fun setupObservers() {
        viewModel.inputs.observe(viewLifecycleOwner, Observer {
            binding.selectedInputView.text = ("Inputs selected: ${it.savedInputsTitle}")
        })
    }

    private fun showDialog(position: Int) {
        val dialog = activity?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.save_options_dialog)
        //dialog.text_dialog.text = ("Do wish to load ${adapter.getItem(position).savedInputsTitle} Inputs?")
        val navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)
        val loadButton = dialog.loadBtn
        val editButton = dialog.editBtn
        val deleteButton = dialog.deleteBtn
        dialog.show()

        loadButton.setOnClickListener {
            viewModel.inputs.value = adapter.getItem(position)
            adapter.notifyDataSetChanged()
            //load inputs, re-save to change timestamp. seems redundant
            GlobalScope.launch(Dispatchers.Main){
                viewModel.insertInputs(
                    Input(
                        savedInputsTitle = viewModel.inputs.value!!.savedInputsTitle,
                        loanAmount = viewModel.inputs.value!!.loanAmount,
                        interestAmount = viewModel.inputs.value!!.interestAmount,
                        yearAmount = viewModel.inputs.value!!.yearAmount,
                        downAmount = viewModel.inputs.value!!.downAmount,
                        yearSpinnerPos = viewModel.inputs.value!!.yearSpinnerPos,
                        startDateFormat = viewModel.inputs.value!!.startDateFormat,
                        payments = viewModel.inputs.value!!.payments,
                        extraPaymentSize = viewModel.inputs.value!!.extraPaymentSize,
                        modifiedAt = System.currentTimeMillis()
                    )
                )
            }
            //order is very important
            viewModel.calculate(AmortizationCalculator())
            dummyInputs.clear()
            adapter.notifyDataSetChanged()
            updateList()
            dialog.dismiss()

        }

        editButton.setOnClickListener {
            viewModel.inputs.value = adapter.getItem(position)
            adapter.notifyItemChanged( position )
            navController.navigate(R.id.action_saveFragment_to_saveDetailFragment)
            dialog.dismiss()
        }

        deleteButton.setOnClickListener {
            viewModel.inputs.value = adapter.getItem(position)
            setupObservers()
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.deleteInput(viewModel.inputs.value!!)
                adapter.notifyDataSetChanged()
                updateList()
            }
            dialog.dismiss()
        }



    }
}










