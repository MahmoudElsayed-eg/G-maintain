package com.example.g_maintain.ui.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import com.example.g_maintain.db.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class BuildContractViewModel(app: Application) : AndroidViewModel(app) {
    private val visitDao = MyDatabase(app).getVisitsDao()

    fun getVisitsById(id: Int): Flow<PagingData<Visit>> {
        return Pager(
            PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            )
        ) {
            visitDao.searchById(id)
        }.flow
    }

}