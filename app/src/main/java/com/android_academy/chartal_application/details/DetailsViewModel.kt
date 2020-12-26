package com.android_academy.chartal_application.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.util.IResProvider
import kotlinx.coroutines.launch

private const val ERROR_LOAD_MOVIES = "Error load movies"

class DetailsViewModel(private val resProvider: IResProvider) : ViewModel() {

    private val _items = MutableLiveData<List<Movie>?>()
    val items: LiveData<List<Movie>?> get() = _items

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        viewModelScope.launch {
            try {
                _items.value = resProvider.loadFilms()
            } catch (error: Throwable) {
                _error.value = ERROR_LOAD_MOVIES
            }

        }
    }
}