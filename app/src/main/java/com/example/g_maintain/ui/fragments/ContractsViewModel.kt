package com.example.g_maintain.ui.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.g_maintain.db.Contract
import com.example.g_maintain.db.MyDatabase
import com.example.g_maintain.db.Visit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ContractsViewModel(app:Application) : AndroidViewModel(app) {
    private val visitDao = MyDatabase(app).getVisitsDao()
    private val contractsDao = MyDatabase(app).getContractDao()
    fun getVisitsById(id: Int) : Flow<PagingData<Visit>> {
        return Pager(
            PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            )
        ){
            visitDao.searchById(id)
        }.flow
    }

    fun saveContract(contract: Contract) : MutableLiveData<String> {
         viewModelScope.launch(Dispatchers.IO) {
             contractsDao.addContract(contract)
         }
        return MutableLiveData("done")
    }
    fun updateContract(contract: Contract) : MutableLiveData<String> {
        viewModelScope.launch(Dispatchers.IO) {
            contractsDao.updateContract(contract)
        }
        return MutableLiveData("done")
    }
}