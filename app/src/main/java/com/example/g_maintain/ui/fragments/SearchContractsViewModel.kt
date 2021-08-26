package com.example.g_maintain.ui.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.g_maintain.db.Contract
import com.example.g_maintain.db.MyDatabase
import com.example.g_maintain.db.Visit
import kotlinx.coroutines.flow.Flow

class SearchContractsViewModel(app:Application) : AndroidViewModel(app) {
    private val contractsDao = MyDatabase(app).getContractDao()
    fun searchByTel(phone : String): Flow<PagingData<Contract>> {
        return Pager(
            PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            )
        ){
            contractsDao.searchContractByPhone(phone)
        }.flow
    }
    fun searchByName(name: String): Flow<PagingData<Contract>> {
        return Pager(
            PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            )
        ){
            contractsDao.searchContractByName(name)
        }.flow
    }
    fun searchByNameAndPhone(name : String, phone: String): Flow<PagingData<Contract>> {
        return Pager(
            PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            )
        ){
            contractsDao.searchContractByPhoneAndName(phone,name)
        }.flow
    }
    fun getAll() : Flow<PagingData<Contract>> {
        return Pager(
            PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            )
        ){
            contractsDao.getAllContracts()
        }.flow
    }

}