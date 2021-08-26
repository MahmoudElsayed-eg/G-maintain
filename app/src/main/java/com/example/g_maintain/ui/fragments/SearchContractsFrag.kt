package com.example.g_maintain.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.g_maintain.R
import com.example.g_maintain.adapters.AutoCompleteClientsArrayAdapter
import com.example.g_maintain.adapters.ContractAdapter
import com.example.g_maintain.databinding.FragmentSearchContractsBinding
import com.example.g_maintain.db.Contract
import com.example.g_maintain.util.FactoryToPassApp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class SearchContractsFrag : Fragment(R.layout.fragment_search_contracts),
    ContractAdapter.ContractClicked , CoroutineScope by MainScope() {
    private lateinit var binding: FragmentSearchContractsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchContractsBinding.bind(view)
        val contractAdapter = ContractAdapter(this)
        val viewModel = ViewModelProvider(
            this,
            FactoryToPassApp { SearchContractsViewModel(requireActivity().application) })[SearchContractsViewModel::class.java]
        var nameAndPhoneJob: Job? = null
        var phoneJob: Job? = null
        var nameJob: Job? = null
        var allJob: Job? = null
        val nameAdapter = AutoCompleteClientsArrayAdapter(requireContext())
        binding.apply {
            contractsSearchRv.apply {
                adapter = contractAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            edtNameSearch.setAdapter(nameAdapter)
            btnSearchContract.setOnClickListener {
                val nameBoolean = edtNameSearch.text.isNullOrEmpty()
                val phoneBoolean = edtPhoneSearch.editText?.text.isNullOrEmpty()
                when {
                    !nameBoolean && !phoneBoolean -> {
                        allJob?.cancelChildren()
                        nameJob?.cancelChildren()
                        phoneJob?.cancelChildren()
                        nameAndPhoneJob = lifecycleScope.launch {
                            contractAdapter.submitData(PagingData.empty())
                            Toast.makeText(requireContext(), "تم انتهاء البحث", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.searchByNameAndPhone(
                                edtNameSearch.text.toString(),
                                edtPhoneSearch.editText?.text.toString()
                            ).collectLatest {
                                contractAdapter.submitData(it)
                            }
                        }
                    }
                    nameBoolean && !phoneBoolean -> {
                        allJob?.cancelChildren()
                        nameAndPhoneJob?.cancelChildren()
                        nameJob?.cancelChildren()
                        phoneJob = lifecycleScope.launch {
                            contractAdapter.submitData(PagingData.empty())
                            Toast.makeText(requireContext(), "تم انتهاء البحث", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.searchByTel(edtPhoneSearch.editText?.text.toString())
                                .collectLatest {
                                    contractAdapter.submitData(it)
                                }
                        }
                    }
                    !nameBoolean && phoneBoolean -> {
                        allJob?.cancelChildren()
                        nameAndPhoneJob?.cancelChildren()
                        phoneJob?.cancelChildren()
                        nameJob = lifecycleScope.launch {
                            contractAdapter.submitData(PagingData.empty())
                            Toast.makeText(requireContext(), "تم انتهاء البحث", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.searchByName(edtNameSearch.text.toString())
                                .collectLatest {
                                    contractAdapter.submitData(it)
                                }
                        }
                    }
                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "برجاء ادخال الاسم أو التليفون",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            btnSearchAll.setOnClickListener {
                nameAndPhoneJob?.cancelChildren()
                phoneJob?.cancelChildren()
                nameJob?.cancelChildren()
                allJob = lifecycleScope.launch {
                    contractAdapter.submitData(PagingData.empty())
                    Toast.makeText(requireContext(), "تم انتهاء البحث", Toast.LENGTH_SHORT).show()
                    viewModel.getAll().collectLatest {
                        contractAdapter.submitData(it)
                    }
                }
            }
        }
    }

    override fun onClientClicked(contract: Contract) {
        val action = SearchContractsFragDirections.actionSearchContractsFragToBuildContractsFrag()
        action.contract = contract
        findNavController().navigate(action)
    }
}