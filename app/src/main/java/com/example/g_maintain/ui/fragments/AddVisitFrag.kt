package com.example.g_maintain.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.g_maintain.databinding.FragmentAddVisitBinding
import com.example.g_maintain.db.MyDatabase
import com.example.g_maintain.db.Visit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AddVisitFrag : Fragment() {
    private lateinit var binding: FragmentAddVisitBinding
    private var visit: Visit? = null
    private var contractId = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddVisitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            visit = AddVisitFragArgs.fromBundle(it).visit
            contractId = AddVisitFragArgs.fromBundle(it).contractId
            if (visit != null) {
                binding.apply {
                    edtDescriptionVisit.editText?.setText(visit?.description)
                    edtVisitName.editText?.setText(visit?.name)
                    edtNotesVisit.editText?.setText(visit?.notes)
                    edtPriceVisit.editText?.setText(visit?.price.toString())
                    txtDateAddVisit.text = visit?.date
                    switchStatus.isChecked = visit!!.done
                }
            }
        }
        binding.apply {
            btnDateAddVisit.setOnClickListener {
                val c = Calendar.getInstance()
                val mYear = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    txtDateAddVisit.visibility = View.VISIBLE
                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    format.format(calendar.time)
                    txtDateAddVisit.text = format.format(calendar.time)
                }, mYear, month, day)
                dpd.show()
            }
            btnSaveVisit.setOnClickListener {
                val name = edtVisitName.editText?.text.toString()
                val desc = edtDescriptionVisit.editText?.text.toString()
                val notes = edtNotesVisit.editText?.text.toString()
                var price = edtPriceVisit.editText?.text.toString()
                val date = txtDateAddVisit.text.toString()
                val status = switchStatus.isChecked
                if (name.isEmpty()) {
                    edtVisitName.error = "مطلوب"
                    return@setOnClickListener
                }
                if (price.isEmpty()) {
                    price = "0"
                }
                val mVisit = Visit(
                    name = name,
                    description = desc,
                    notes = notes,
                    price = price.toInt(),
                    date = date,
                    contractId = contractId,
                    done = status
                )
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        if (visit == null) {
                            MyDatabase(requireContext()).getVisitsDao().addVisit(mVisit)
                        } else {
                            mVisit.id = visit!!.id
                            MyDatabase(requireContext()).getVisitsDao().updateVisit(mVisit)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        if (visit == null) {
                            Toast.makeText(requireContext(), "تم الحفظ", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "تم التعديل", Toast.LENGTH_SHORT)
                                .show()
                        }
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }
}