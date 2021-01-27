package com.android_academy.chartal_application.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android_academy.chartal_application.repository.FilmsRepository
import com.android_academy.chartal_application.util.IResProvider
import com.android_academy.chartal_application.util.NetworkStatus

class MoviesViewModelFactory(
    private val filmsRepository: FilmsRepository,
    private val resProvider: IResProvider,
    private val networkStatus: NetworkStatus
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesViewModel::class.java -> MoviesViewModel(filmsRepository, resProvider, networkStatus)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}