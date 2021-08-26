package com.example.g_maintain.ui.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.g_maintain.db.MyDatabase
import com.example.g_maintain.db.Visit
import kotlinx.coroutines.flow.Flow

class SearchVisitsViewModel(app:Application) : AndroidViewModel(app) {
    private val visitDao = MyDatabase(app).getVisitsDao()
    fun getVisitsByDate(date : String): Flow<PagingData<Visit>> {
        return Pager(
            PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            )
        ){
            visitDao.searchByDate(date)
        }.flow
    }
    fun searchVisitsByDateAndRegion(region: String,date: String): Flow<PagingData<Visit>> {
        return Pager(
            PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            )
        ){
            visitDao.searchByRegionAndDate(region,date)
        }.flow
    }
    fun getVisitsByRegion(region : String): Flow<PagingData<Visit>> {
        return Pager(
            PagingConfig(
                pageSize = 50,
                enablePlaceholders = true
            )
        ){
            visitDao.searchByRegion(region)
        }.flow
    }

}