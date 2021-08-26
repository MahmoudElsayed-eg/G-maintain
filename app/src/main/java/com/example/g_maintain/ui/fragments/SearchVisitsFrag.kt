package com.example.g_maintain.ui.fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.g_maintain.R
import com.example.g_maintain.adapters.AutoCompleteRegionsArrayAdapter
import com.example.g_maintain.adapters.SearchVisitAdapter
import com.example.g_maintain.adapters.VisitAdapter
import com.example.g_maintain.databinding.FragmentSearchVisitsBinding
import com.example.g_maintain.db.Visit
import com.example.g_maintain.util.FactoryToPassApp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*

class SearchVisitsFrag : Fragment(),SearchVisitAdapter.VisitClicked,CoroutineScope by MainScope(){
    private lateinit var binding: FragmentSearchVisitsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchVisitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = SearchVisitAdapter(this,lifecycleScope,requireContext())
        val viewModel = ViewModelProvider(
            this,
            FactoryToPassApp { SearchVisitsViewModel(requireActivity().application) })[SearchVisitsViewModel::class.java]
        val regionAdapter = AutoCompleteRegionsArrayAdapter(requireContext())
        binding.apply {
            btnDateSearch.setOnClickListener {
                getDate()
            }
            edtRegionSearch.setAdapter(regionAdapter)
            deleteDateSearch.setOnClickListener {
                txtDateSearch.text = ""
                deleteDateSearch.visibility = View.GONE
            }
            visitsSearchRv.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireContext())
                visibility = View.VISIBLE
            }

            var dateAndRegionJob: Job? = null
            var dateJob : Job?= null
            var regionJob: Job? = null

            btnSearchVisits.setOnClickListener {
            val dateBoolean = txtDateSearch.text.isNullOrEmpty()
            val regionBoolean = edtRegionSearch.text.isNullOrEmpty()
                searchVisitsCard.visibility = View.VISIBLE
                when {
                    !dateBoolean && !regionBoolean -> {
                        dateJob?.cancelChildren()
                        regionJob?.cancelChildren()
                        dateAndRegionJob = launch {
                            mAdapter.submitData(PagingData.empty())
                            Toast.makeText(requireContext(), "تم انتهاء البحث", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.searchVisitsByDateAndRegion(
                                edtRegionSearch.text.toString(),
                                txtDateSearch.text.toString()
                            ).collectLatest {
                                mAdapter.submitData(it)
                            }
                        }

                    }
                    !dateBoolean && regionBoolean -> {
                        dateAndRegionJob?.cancelChildren()
                        regionJob?.cancelChildren()
                        dateJob = launch {
                            mAdapter.submitData(PagingData.empty())
                            Toast.makeText(requireContext(), "تم انتهاء البحث", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.getVisitsByDate(txtDateSearch.text.toString()).collectLatest {
                                mAdapter.submitData(it)
                            }
                        }
                    }
                    dateBoolean && !regionBoolean -> {
                        dateJob?.cancelChildren()
                        dateAndRegionJob?.cancelChildren()
                        regionJob = launch {
                            mAdapter.submitData(PagingData.empty())
                            Toast.makeText(requireContext(), "تم انتهاء البحث", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.getVisitsByRegion(edtRegionSearch.text.toString()).collectLatest {
                                mAdapter.submitData(it)
                            }
                        }
                    }
                    else -> {
                        searchVisitsCard.visibility = View.GONE
                        Toast.makeText(requireContext(),"برجاء ادخال المنطقة أو التاريخ",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onVisitClicked(visit: Visit) {
        val action = SearchVisitsFragDirections.actionSearchVisitsFragToAddContractsFrag()
        action.visit = visit
        action.contractId = visit.contractId
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    private fun getDate() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            binding.txtDateSearch.visibility = View.VISIBLE
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            format.format(calendar.time)
            binding.txtDateSearch.text = format.format(calendar.time)
            binding.deleteDateSearch.visibility = View.VISIBLE
        }, mYear, month, day)
        dpd.show()
    }
}