package com.example.g_maintain.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.g_maintain.ui.fragments.ClientsViewModel

class FactoryToPassApp<VM: ViewModel>(val provider: () -> VM): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  provider() as T
    }
}