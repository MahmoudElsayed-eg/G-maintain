package com.example.g_maintain.ui.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.g_maintain.db.MyDatabase

class RegionsViewModel(app:Application) : AndroidViewModel(app) {
    private val regionDao = MyDatabase(app).getRegionDao()
    val clients = Pager(
        PagingConfig(
            pageSize = 50,
            enablePlaceholders = true
        )
    ){
        regionDao.getAllRegions()
    }.flow
}