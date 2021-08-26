package com.example.g_maintain.ui.fragments

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.g_maintain.adapters.AutoCompleteClientsArrayAdapter
import com.example.g_maintain.adapters.AutoCompleteRegionsArrayAdapter
import com.example.g_maintain.adapters.VisitAdapter
import com.example.g_maintain.databinding.FragmentBuildContractBinding
import com.example.g_maintain.db.Client
import com.example.g_maintain.db.Contract
import com.example.g_maintain.db.MyDatabase
import com.example.g_maintain.db.Visit
import com.example.g_maintain.util.FactoryToPassApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BuildContractsFrag : Fragment(), VisitAdapter.VisitClicked {
    private lateinit var binding: FragmentBuildContractBinding
    private var contract: Contract? = null
    private var mainContract: Contract? = null
    private var contractId: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuildContractBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(
            this,
            FactoryToPassApp { BuildContractViewModel(requireActivity().application) })[BuildContractViewModel::class.java]
        val mAdapter = VisitAdapter(this)
        arguments?.let {
            contract = BuildContractsFragArgs.fromBundle(it).contract
            if (contract != null) {

                lifecycleScope.launch {

                    withContext(Dispatchers.IO) {
                        val name = MyDatabase(requireContext()).getClientDao()
                            .getClientById(contract!!.clientId)?.name
                        withContext(Dispatchers.Main) {
                            binding.autoCompleteClient.setText(name)
                        }
                    }
                    viewModel.getVisitsById(contract!!.id).collectLatest { data ->
                        binding.visitsRvBuild.apply {
                            adapter = mAdapter
                            layoutManager = LinearLayoutManager(requireContext())
                            visibility = View.VISIBLE
                        }
                        mAdapter.submitData(data)
                    }
                }
                binding.apply {
                    edtContractName.editText?.setText(contract!!.name)
                    autoCompleteRegion.setText(contract!!.region)
                    editTextLikeTextView()
                    btnBuildContractBuild.visibility = View.GONE
                    btnDeleteContract.visibility = View.VISIBLE
                    btnFirstDateBuild.visibility = View.GONE
                    cardBuild.visibility = View.VISIBLE
                    btnAddVisitBuild.visibility = View.VISIBLE
                    btnEditContract.visibility = View.VISIBLE
                }
            }
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                mainContract =
                    MyDatabase(requireContext()).getContractDao().getContractById(1)
            }
        }

        val autoCompleteClientsArrayAdapter = AutoCompleteClientsArrayAdapter(requireContext())
        val autoCompleteRegionsArrayAdapter = AutoCompleteRegionsArrayAdapter(requireContext())
        binding.apply {
            autoCompleteClient.apply {
                setAdapter(autoCompleteClientsArrayAdapter)
                addTextChangedListener {
                    edtContractName.editText?.setText("عقد " + it.toString())
                }
            }
            autoCompleteRegion.setAdapter(autoCompleteRegionsArrayAdapter)
            binding.visitsRvBuild.apply {
                adapter = mAdapter
                layoutManager =
                    LinearLayoutManager(requireContext())
            }
            btnBuildContractBuild.setOnClickListener {
                if (txtFirstDateBuild.text.isEmpty()) {
                    Toast.makeText(requireContext(), "ادخل التاريخ", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (autoCompleteClient.text.isNullOrEmpty()) {
                    autoCompleteClient.error = "ادخل الاسم"
                    return@setOnClickListener
                }
                if (autoCompleteRegion.text.isNullOrEmpty()) {
                    autoCompleteRegion.error = "ادخل المنطقة"
                    return@setOnClickListener
                }
                if (edtContractName.editText?.text.isNullOrEmpty()) {
                    edtContractName.editText?.error = "ادخل اسم العقد"
                    return@setOnClickListener
                }
                if (mainContract == null) {
                    Toast.makeText(
                        requireContext(),
                        "تأكد من ادخال العقد الأساسي",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                btnBuildContractBuild.visibility = View.GONE
                lifecycleScope.launch {
                    var client: Client? = null
                    withContext(Dispatchers.IO) {
                        client = MyDatabase(requireContext()).getClientDao()
                            .getClientByName(autoCompleteClient.text.toString())
                    }
                    if (client == null) {
                        AlertDialog.Builder(requireContext()).apply {
                            setMessage("لا يوجد عميل بهذا الاسم قم بانشاء عميل؟")
                            setNegativeButton("لا") { dialog, _ -> dialog.dismiss() }
                            setPositiveButton("الذهاب الى صفحة الانشاء") { _, _ -> goToAddClient() }
                            show()
                        }
                        btnBuildContractBuild.visibility = View.VISIBLE
                    } else {
                        launch(Dispatchers.IO) {
                            val clientContract = MyDatabase(requireContext()).getContractDao()
                                .getContractByClientId(client!!.id)
                            withContext(Dispatchers.Main) {
                                if (clientContract != null) {
//                                    viewModel.getVisitsById(contract!!.id).collectLatest { data ->
//
//                                        mAdapter.submitData(data)
//                                    }
//                                    binding.apply {
//                                        autoCompleteRegion.setText(contract!!.region)
//                                        btnBuildContractBuild.visibility = View.GONE
//                                        btnDeleteContract.visibility = View.VISIBLE
//                                        btnFirstDateBuild.visibility = View.GONE
//                                    }
                                    AlertDialog.Builder(requireContext()).setNegativeButton(
                                        "تجاهل"
                                    ) { dialogInterface: DialogInterface, _: Int ->
                                        dialogInterface.dismiss()
                                        btnBuildContractBuild.visibility = View.VISIBLE
                                    }
                                        .setPositiveButton("انشاء عقد جديد") { _: DialogInterface, _: Int ->
                                            lifecycleScope.launch {
                                                withContext(Dispatchers.IO) {
                                                    contract = Contract(
                                                        0,
                                                        edtContractName.editText?.text.toString(),
                                                        mainContract!!.period,
                                                        txtFirstDateBuild.text.toString(),
                                                        client!!.id,
                                                        autoCompleteRegion.text.toString()
                                                    )
                                                    contractId =
                                                        MyDatabase(requireContext()).getContractDao()
                                                            .addContract(contract!!)
                                                    withContext(Dispatchers.Main) {
                                                        createVisitsFromId(
                                                            contractId.toInt(),
                                                            txtFirstDateBuild.text.toString()
                                                        )
                                                        editTextLikeTextView()
                                                        contract!!.id = contractId.toInt()
                                                        btnFirstDateBuild.visibility = View.GONE
                                                        txtFirstDateBuild.visibility = View.GONE
                                                        visitsRvBuild.visibility =
                                                            View.VISIBLE
                                                        cardBuild.visibility = View.VISIBLE
                                                        btnAddVisitBuild.visibility =
                                                            View.VISIBLE
                                                        btnEditContract.visibility = View.VISIBLE
                                                        btnDeleteContract.visibility =
                                                            View.VISIBLE
                                                        btnBuildContractBuild.visibility =
                                                            View.GONE
                                                        viewModel.getVisitsById(contractId.toInt())
                                                            .collectLatest { data ->
                                                                mAdapter.submitData(data)
                                                            }
                                                    }

                                                }
                                            }
                                        }
                                        .setMessage("يوجد عقد لهذا العميل هل تريد انشاء عقد جديد ؟")
                                        .setCancelable(false)
                                        .show()
                                } else {
                                    withContext(Dispatchers.IO) {
                                        contract = Contract(
                                            0,
                                            edtContractName.editText?.text.toString(),
                                            mainContract!!.period,
                                            txtFirstDateBuild.text.toString(),
                                            client!!.id,
                                            autoCompleteRegion.text.toString()
                                        )

                                        contractId = MyDatabase(requireContext()).getContractDao()
                                            .addContract(contract!!)
                                        withContext(Dispatchers.Main) {
                                            createVisitsFromId(
                                                contractId.toInt(),
                                                txtFirstDateBuild.text.toString()
                                            )
                                            editTextLikeTextView()
                                            contract!!.id = contractId.toInt()
                                            btnEditContract.visibility = View.VISIBLE
                                            btnFirstDateBuild.visibility = View.GONE
                                            txtFirstDateBuild.visibility = View.GONE
                                            visitsRvBuild.visibility = View.VISIBLE
                                            cardBuild.visibility = View.VISIBLE
                                            btnAddVisitBuild.visibility = View.VISIBLE
                                            btnDeleteContract.visibility = View.VISIBLE
                                            btnBuildContractBuild.visibility = View.GONE
                                            viewModel.getVisitsById(contractId.toInt())
                                                .collectLatest { data ->
                                                    mAdapter.submitData(data)
                                                }
                                        }
                                    }

                                }
                            }
                        }
//                        launch(Dispatchers.IO) {
//                            contract = Contract(
//                                client!!.id,
//                                client!!.name,
//                                mainContract!!.period,
//                                txtFirstDateBuild.text.toString(),
//                                client!!.id,
//                                autoCompleteRegion.text.toString()
//                            )
//                            MyDatabase(requireContext()).getContractDao().addContract(contract!!)
//                        }
//                        launch {
//                            createVisitsFromId(client!!.id, txtFirstDateBuild.text.toString())
//                            visitsRvBuild.visibility = View.VISIBLE
//                            cardBuild.visibility = View.VISIBLE
//                            btnAddVisitBuild.visibility = View.VISIBLE
//                            btnDeleteContract.visibility = View.VISIBLE
//                            btnBuildContractBuild.visibility = View.GONE
//                            progressBarBuild.visibility = View.GONE
//                            viewModel.getVisitsById(client!!.id).collectLatest { data ->
//                                binding.visitsRvBuild.apply {
//                                    adapter = mAdapter
//                                    layoutManager = LinearLayoutManager(requireContext())
//                                }
//                                mAdapter.submitData(data)
//                            }
//                        }
                    }
                }
            }
            btnAddVisitBuild.setOnClickListener {
                val action =
                    BuildContractsFragDirections.actionBuildContractsFragToAddContractsFrag()
                action.contractId = contract!!.id
                findNavController().navigate(action)
            }
            btnDeleteContract.setOnClickListener {
                AlertDialog.Builder(requireContext()).setMessage("تأكيد مسح العقد ؟")
                    .setPositiveButton("تأكيد") { _, _ ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            MyDatabase(requireContext()).getContractDao().deleteContract(contract!!)
                            MyDatabase(requireContext()).getVisitsDao().deleteById(contract!!.id)
                            withContext(Dispatchers.Main.immediate) {
                                Toast.makeText(requireContext(), "تم مسح العقد", Toast.LENGTH_SHORT)
                                    .show()
                                findNavController().navigateUp()
                            }
                        }
                    }.setNegativeButton("تجاهل") { dialog, _ ->
                    dialog.dismiss()
                }.show()
            }
            btnEditContract.setOnClickListener {
                if (edtContractName.editText?.text.isNullOrBlank()) {
                    edtContractName.editText?.error = "مطلوب"
                    return@setOnClickListener
                }
                val updatedContract = Contract(
                    contract!!.id,
                    edtContractName.editText?.text.toString(),
                    contract!!.period,
                    contract!!.firstDate,
                    contract!!.clientId,
                    contract!!.region
                )
                lifecycleScope.launch(Dispatchers.IO) {
                    MyDatabase(requireContext()).getContractDao().updateContract(updatedContract)
                    withContext(Dispatchers.Main.immediate) {
                        Toast.makeText(requireContext(), "تم التعديل", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnFirstDateBuild.setOnClickListener {
                val c = Calendar.getInstance()
                val mYear = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    txtFirstDateBuild.visibility = View.VISIBLE
                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    format.format(calendar.time)
                    txtFirstDateBuild.text = format.format(calendar.time)
                }, mYear, month, day)
                dpd.show()
            }
        }

    }

    override fun onVisitClicked(visit: Visit) {
        val action = BuildContractsFragDirections.actionBuildContractsFragToAddContractsFrag()
        action.visit = visit
        action.contractId = visit.contractId
        findNavController().navigate(action)
    }

    private fun createVisitsFromId(id: Int, date: String) {
        val visitDao = MyDatabase(requireContext()).getVisitsDao()
        lifecycleScope.launch(Dispatchers.IO) {
            val visits = visitDao.searchByIdList(1)
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val calendar = Calendar.getInstance()
            calendar.time = format.parse(date)!!
            for (i in visits) {
                val mDate = format.format(calendar.time)
                val visit = Visit(
                    contractId = id,
                    name = i.name,
                    description = i.description,
                    price = i.price,
                    notes = i.notes,
                    done = i.done,
                    date = mDate
                )
                visitDao.addVisit(visit)
                calendar.add(Calendar.DATE, mainContract!!.period)
            }
        }
    }

    private fun goToAddClient() {
        val action = BuildContractsFragDirections.actionBuildContractsFragToAddClientFrag()
        findNavController().navigate(action)
    }

    private fun editTextLikeTextView() {
        binding.autoCompleteRegion.apply {
            isCursorVisible = false
            isLongClickable = false
            isClickable = false
            isFocusable = false
            isSelected = false
            keyListener = null
        }
        binding.autoCompleteClient.apply {
            isCursorVisible = false
            isLongClickable = false
            isClickable = false
            isFocusable = false
            isSelected = false
            keyListener = null
        }
    }


}
