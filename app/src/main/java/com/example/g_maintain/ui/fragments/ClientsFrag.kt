package com.example.g_maintain.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.g_maintain.R
import com.example.g_maintain.adapters.ClientAdapter
import com.example.g_maintain.databinding.FragmentClientsBinding
import com.example.g_maintain.db.Client
import com.example.g_maintain.util.FactoryToPassApp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ClientsFrag : Fragment(R.layout.fragment_clients), ClientAdapter.ClientClicked {
    private lateinit var binding: FragmentClientsBinding
    private lateinit var viewModel: ClientsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentClientsBinding.bind(view)
        viewModel = ViewModelProvider(this, FactoryToPassApp{ClientsViewModel(requireActivity().application)})[ClientsViewModel::class.java]
        val clientAdapter = ClientAdapter(this)
        lifecycleScope.launch {
            viewModel.clients.collectLatest {
                binding.apply {
                    clientsRv.apply {
                        visibility = View.VISIBLE
                        adapter = clientAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }
                clientAdapter.submitData(it)
            }
        }
        binding.btnAddClient.setOnClickListener {
            val action = ClientsFragDirections.actionClientsFragToAddClientFrag()
            findNavController().navigate(action)
        }
    }

    override fun onClientClicked(client: Client) {
        val action = ClientsFragDirections.actionClientsFragToAddClientFrag()
        action.client = client
        findNavController().navigate(action)
    }
}