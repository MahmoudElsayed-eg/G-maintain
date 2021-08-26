package com.example.g_maintain.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.g_maintain.R
import com.example.g_maintain.databinding.FragmentAddRegionsBinding
import com.example.g_maintain.db.MyDatabase
import com.example.g_maintain.db.Region
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddRegionsFrag : Fragment(R.layout.fragment_add_regions) {
    private lateinit var binding: FragmentAddRegionsBinding
    private var region: Region? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddRegionsBinding.bind(view)
        arguments?.let {
            region = AddRegionsFragArgs.fromBundle(it).region
            binding.edtRegionAdd.editText?.setText(region?.regionName)
        }
        binding.apply {
            btnSaveRegion.setOnClickListener {
                val regionName = edtRegionAdd.editText?.text.toString()
                if (regionName.isEmpty()) {
                    edtRegionAdd.error = "مطلوب"
                    return@setOnClickListener
                }
                saveRegion()
            }
        }
    }

    private fun saveRegion() {

        val regionName = binding.edtRegionAdd.editText?.text.toString()
        val mRegion = Region(regionName = regionName)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                if (region == null) {
                    MyDatabase(requireContext()).getRegionDao().addRegion(mRegion)
                } else {
                    mRegion.id = region!!.id
                    MyDatabase(requireContext()).getRegionDao().updateRegion(mRegion)
                }
            }
            withContext(Dispatchers.Main) {
                if (region == null) {
                    Toast.makeText(requireContext(), "تم الحفظ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "تم التعديل", Toast.LENGTH_SHORT).show()
                }
                findNavController().navigateUp()
            }
        }
    }
}