package com.example.g_maintain.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.g_maintain.R
import com.example.g_maintain.databinding.FragmentMainMenuBinding

class MainMenu : Fragment() {
    private lateinit var binding: FragmentMainMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainMenuBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().actionBar?.setTitle(R.string.app_name)
        binding.apply {
            btnClients.setOnClickListener {
                val action = MainMenuDirections.actionMainMenuToClientsFrag()
                findNavController().navigate(action)
            }
            btnSearchVisitsMain.setOnClickListener {
                val action = MainMenuDirections.actionMainMenuToSearchVisitsFrag()
                findNavController().navigate(action)
            }
            btnBuildContract.setOnClickListener {
                val action = MainMenuDirections.actionMainMenuToBuildContractsFrag()
                findNavController().navigate(action)
            }
            btnContracts.setOnClickListener {
                val action = MainMenuDirections.actionMainMenuToContractsFrag()
                findNavController().navigate(action)
            }
            btnRegions.setOnClickListener {
                val action = MainMenuDirections.actionMainMenuToRegionsFrag()
                findNavController().navigate(action)
            }
            btnSearchContractsMain.setOnClickListener {
                val action = MainMenuDirections.actionMainMenuToSearchContractsFrag()
                findNavController().navigate(action)
            }
            btnInfo.setOnClickListener {
                val fragment = DialogFragmentInfo()
                fragment.show(childFragmentManager,null)
            }
        }

    }
}