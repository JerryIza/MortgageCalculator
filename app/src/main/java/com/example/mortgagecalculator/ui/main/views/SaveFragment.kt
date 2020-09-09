package com.example.mortgagecalculator.ui.main.views

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavAction
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mortgagecalculator.R
import com.example.mortgagecalculator.databinding.SaveFragmentBinding
import com.example.mortgagecalculator.db.Input
import com.example.mortgagecalculator.ui.main.adapters.SaveAdapter
import com.example.mortgagecalculator.ui.main.viewmodels.AmortizationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.save_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import androidx.navigation.findNavController


//R.layout.save_fragment will get deleted
@AndroidEntryPoint
class SaveFragment : Fragment() {

    private lateinit var binding: SaveFragmentBinding
    //by activityViewModels creates view model score for the whole activity. vs by viewmodels = individual fragment
    private val viewModel: AmortizationViewModel by activityViewModels()
    private lateinit var adapter: SaveAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SaveFragmentBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupObservers()
        //showButtons()
        val input: Input = viewModel.inputs.value!!


        binding.saveBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.insertInputs(input)
                //showButtons()
            }
        }

        binding.deleteBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                viewModel.deleteAllInputs()
                // showButtons()
            }
        }

        val navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)

        binding.floatingSaveButton.setOnClickListener {
            navController.navigate(R.id.action_saveFragment_to_saveDetailFragment)
        }


        return binding.root
    }


    private fun setupRecyclerView() {
        val dummyInputs = arrayListOf(
            Input(25000.00, 30)
        )
        adapter = SaveAdapter(dummyInputs) {
            Toast.makeText(activity, "Position clicked: " + it.let { adapter.getItem(it)}, Toast.LENGTH_SHORT).show()

            // how we choose results.
            viewModel.inputs.value = adapter.getItem(it)

        }
        binding.saveRv.layoutManager = LinearLayoutManager(requireContext())
        binding.saveRv.adapter = adapter
        adapter.notifyDataSetChanged()
    }



    private fun setupObservers() {
        viewModel.liveInputs.observe(viewLifecycleOwner, Observer {
            adapter.setItems(ArrayList(it))
        })
    }


}

//make sure it becomes an observer later
/*private fun showButtons() {
     GlobalScope.launch(Dispatchers.Main) {
         val test = withContext(Dispatchers.Default) { viewModel.databaseSize() }
         if (test == 0) {
             binding.saveBtn.visibility = View.VISIBLE
             binding.deleteBtn.visibility = View.INVISIBLE
             textView.text = viewModel.getAllInputs().toString()
             println("execute below minimum")
         } else {
             binding.saveBtn.visibility = View.INVISIBLE
             binding.deleteBtn.visibility = View.VISIBLE
             textView.text = viewModel.getAllInputs().toString()
             println("executed not empty")
         }
     }
 }*/








