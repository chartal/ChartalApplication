package com.android_academy.chartal_application.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.repository.FilmsRepository

import com.android_academy.chartal_application.util.IResProvider
import kotlinx.coroutines.launch

private const val ERROR_LOAD_MOVIES = "Error load movies"

class MoviesViewModel(private val filmsRepository: FilmsRepository, private val resProvider: IResProvider) : ViewModel() {

    private val _items = MutableLiveData<List<Movie>>()
    val items: LiveData<List<Movie>> get() = _items

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> get() = _movie

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val isProgressBarVisibleMutableLiveData = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean> = isProgressBarVisibleMutableLiveData

    private var page: Int = 1
    private val myList = mutableListOf<Movie>()
    private var flag = true

    init {
        viewModelScope.launch {
            try {
                isProgressBarVisibleMutableLiveData.value = true
                myList.addAll(filmsRepository.getListOfFilms(page))
                _items.value = myList
            } catch (error: Throwable) {
                _error.value = ERROR_LOAD_MOVIES
            } finally {
                isProgressBarVisibleMutableLiveData.value = false
            }
        }
    }

    fun getSearchMovie(query: String) {
        flag = false
        viewModelScope.launch {
            try {
                myList.clear()
                myList.addAll(filmsRepository.getListOfFilms2(query))
                _items.value = myList
            } catch (error: Throwable) {
                _error.value = ERROR_LOAD_MOVIES
            }
        }
    }

    fun getDefaultList(){
        page=1
        flag=true
        myList.clear()
        viewModelScope.launch {
            try {
                isProgressBarVisibleMutableLiveData.value = true
                myList.addAll(filmsRepository.getListOfFilms(page))
                _items.value = myList
            } catch (error: Throwable) {
                _error.value = ERROR_LOAD_MOVIES
            } finally {
                isProgressBarVisibleMutableLiveData.value = false
            }
        }
    }

    fun addNextListOfMovies() {
        if (flag) {
            if (page <= 3) {
                page++
                viewModelScope.launch {
                    try {
                        isProgressBarVisibleMutableLiveData.value = true
                        myList.addAll(filmsRepository.getListOfFilms(page))
                        _items.value = myList
                    } catch (error: Throwable) {
                        _error.value = ERROR_LOAD_MOVIES
                    } finally {
                        isProgressBarVisibleMutableLiveData.value = false
                    }
                }
            }
        }
    }
}