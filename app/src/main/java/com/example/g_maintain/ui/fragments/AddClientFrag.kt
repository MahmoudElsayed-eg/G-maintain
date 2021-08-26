package com.example.g_maintain.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.g_maintain.databinding.FragmentAddClientBinding
import com.example.g_maintain.db.Client
import com.example.g_maintain.db.MyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddClientFrag : Fragment() {
    private lateinit var binding: FragmentAddClientBinding
    private var client:Client? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddClientBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            client = AddClientFragArgs.fromBundle(it).client
            setClientEditTexts(client)
        }
        binding.apply {
            btnSaveClient.setOnClickListener {
                if (edtPhone.editText?.text.toString().isEmpty()) {
                    edtPhone.error = "مطلوب"
                    return@setOnClickListener
                }
                if (edtClientName.editText?.text.toString().isEmpty()) {
                    edtClientName.error = "مطلوب"
                    return@setOnClickListener
                }
                saveClient()
            }
        }
    }
    private fun setClientEditTexts(client: Client?) {
        binding.apply {
            edtClientName.editText?.setText(client?.name)
            edtAddress.editText?.setText(client?.address)
            edtPhone.editText?.setText(client?.phone)
            edtPhone2.editText?.setText(client?.phone2)
            edtNotes.editText?.setText(client?.notes)
            edtSuperVisorName.editText?.setText(client?.superVisor)
        }
    }

    private fun saveClient() {
        binding.apply {
            val name = edtClientName.editText?.text.toString()
            val superVisor = edtSuperVisorName.editText?.text.toString()
            val phone = edtPhone.editText?.text.toString()
            val phone2 = edtPhone2.editText?.text.toString()
            val address = edtAddress.editText?.text.toString()
            val notes = edtNotes.editText?.text.toString()
            val mClient = Client(name, phone, superVisor, phone2, address, notes)
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    if (client == null) {
                        try {
                            MyDatabase(requireContext()).getClientDao().addClient(mClient)
                        }catch (e:Exception) {
                            AlertDialog.Builder(requireContext()).apply {
                                setMessage("يوجد عميل بنفس الاسم")
                                setNeutralButton("تجاهل") {dialog,_ -> dialog.dismiss()}
                            }
                        }
                    } else {
                        mClient.id = client!!.id
                        MyDatabase(requireContext()).getClientDao().updateClient(mClient)
                    }
                }
                withContext(Dispatchers.Main) {
                    if (client == null) {
                        Toast.makeText(requireContext(), "تم اضافة العميل", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), "تم تحديث العميل", Toast.LENGTH_SHORT)
                            .show()
                    }
                    findNavController().navigateUp()
                }
            }
        }
    }
}