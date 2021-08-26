package com.example.g_maintain.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.g_maintain.R
import com.example.g_maintain.adapters.ClientAdapter
import com.example.g_maintain.adapters.RegionAdapter
import com.example.g_maintain.databinding.FragmentRegionsBinding
import com.example.g_maintain.db.Region
import com.example.g_maintain.util.FactoryToPassApp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegionsFrag : Fragment(R.layout.fragment_regions),RegionAdapter.RegionClicked {
    private lateinit var binding: FragmentRegionsBinding
    private lateinit var viewModel: RegionsViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegionsBinding.bind(view)
        viewModel = ViewModelProvider(this, FactoryToPassApp { RegionsViewModel(requireActivity().application) })[RegionsViewModel::class.java]
        val clientAdapter = RegionAdapter(this)
        lifecycleScope.launch {
            viewModel.clients.collectLatest {
                binding.apply {
                    regionsRv.apply {
                        visibility = View.VISIBLE
                        adapter = clientAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }
                clientAdapter.submitData(it)
            }
        }
        binding.btnAddRegion.setOnClickListener {
            val action = RegionsFragDirections.actionRegionsFragToAddRegionsFrag()
            findNavController().navigate(action)
        }

    }

    override fun onRegionClicked(region: Region) {
        val action = RegionsFragDirections.actionRegionsFragToAddRegionsFrag()
        action.region = region
        findNavController().navigate(action)
    }
}