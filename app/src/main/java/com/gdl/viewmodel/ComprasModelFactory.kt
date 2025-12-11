package com.gdl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gdl.repository.ComprasRepository

class ComprasViewModelFactory(private val repository: ComprasRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComprasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ComprasViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

