package com.example.g_maintain.ui.fragments

import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.g_maintain.R
import com.example.g_maintain.adapters.VisitAdapter
import com.example.g_maintain.databinding.FragmentContractsBinding
import com.example.g_maintain.db.Contract
import com.example.g_maintain.db.MyDatabase
import com.example.g_maintain.db.Visit
import com.example.g_maintain.util.FactoryToPassApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContractsFrag : Fragment(R.layout.fragment_contracts), VisitAdapter.VisitClicked {
    private lateinit var binding: FragmentContractsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(
            this,
            FactoryToPassApp { ContractsViewModel(requireActivity().application) })[ContractsViewModel::class.java]
        binding = FragmentContractsBinding.bind(view)
        val mAdapter = VisitAdapter(this)
        var contract: Contract? = null
        binding.apply {
            btnAddVisit.setOnClickListener {
                val action = ContractsFragDirections.actionContractsFragToAddContractsFrag()
                findNavController().navigate(action)
            }
            visitsRv.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    contract = MyDatabase(requireContext()).getContractDao().getContractById(1)
                }
                if (contract != null) {
                    edtPeriod.editText?.setText(contract!!.period.toString())
                }
                viewModel.getVisitsById(1).collectLatest {
                    mAdapter.submitData(it)
                }
            }
            btnSaveContract.setOnClickListener {
                if (edtPeriod.editText?.text.isNullOrEmpty()) {
                    edtPeriod.editText?.error = "مطلوب"
                    return@setOnClickListener
                }
                val period = edtPeriod.editText?.text.toString()
                val mContract = Contract(0, "العقد الأساسي", period.toInt())
                if (contract != null) {
                    mContract.id = contract?.id!!
                    viewModel.updateContract(mContract).observe(viewLifecycleOwner) {
                        Toast.makeText(requireContext(), "تم التعديل", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    viewModel.saveContract(mContract).observe(viewLifecycleOwner) {
                        Toast.makeText(requireContext(), "تم الحفظ", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(
                            R.id.contractsFrag,
                            arguments,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.contractsFrag, true)
                                .build()
                        )
                    }
                }

            }
        }
    }

    override fun onVisitClicked(visit: Visit) {
        val action = ContractsFragDirections.actionContractsFragToAddContractsFrag()
        action.visit = visit
        findNavController().navigate(action)
    }

}