package com.android_academy.chartal_application.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android_academy.chartal_application.repository.FilmsRepository

class DetailsViewModelFactory(private val filmsRepository: FilmsRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        DetailsViewModel::class.java -> DetailsViewModel(filmsRepository)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}
