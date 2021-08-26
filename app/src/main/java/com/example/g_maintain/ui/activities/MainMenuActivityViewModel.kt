package com.example.g_maintain.ui.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.g_maintain.db.MyDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class MainMenuActivityViewModel(app:Application) : AndroidViewModel(app) {
    private val regionDao = MyDatabase(app).getVisitsDao()
    private val clientDao = MyDatabase(app).getClientDao()
    val regionSearchQuery = MutableStateFlow("")
    val clientSearchQuery = MutableStateFlow("")
    @ExperimentalCoroutinesApi
    private val regionFlow = regionSearchQuery.flatMapLatest {
        Pager(
            PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            )
        ){
            regionDao.searchByRegion(it)
        }.flow
    }
    @ExperimentalCoroutinesApi
    private val clientFlow = clientSearchQuery.flatMapLatest {
        Pager(
            PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            )
        ){
            clientDao.getAllClientsByNamePaging(it)
        }.flow
    }
}