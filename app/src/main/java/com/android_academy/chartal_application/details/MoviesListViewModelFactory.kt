package com.android_academy.chartal_application.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android_academy.chartal_application.notification.NewMovieChecker
import com.android_academy.chartal_application.repository.FilmsRepository
import com.android_academy.chartal_application.util.IResProvider
import com.android_academy.chartal_application.util.NetworkStatus


class MoviesListViewModelFactory(
    private val filmsRepository: FilmsRepository,
    private val resProvider: IResProvider,
    private val networkStatus: NetworkStatus,
    private val newMovieChecker: NewMovieChecker
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesViewModel::class.java -> MoviesViewModel(filmsRepository, resProvider, networkStatus, newMovieChecker)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}