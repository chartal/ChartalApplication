package com.android_academy.chartal_application.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.repository.FilmsRepository
import com.android_academy.chartal_application.util.IResProvider
import com.android_academy.chartal_application.util.NetworkStatus
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ERROR_LOAD_MOVIES = "Error load movies"
private const val LOG_TAG = "Chartal"

class MoviesViewModel(
    private val filmsRepository: FilmsRepository,
    private val resProvider: IResProvider,
    private val networkStatus: NetworkStatus
) : ViewModel() {

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
    private var pagination = true
    private val internetNoAccess: Boolean
        get() = !networkStatus.internetConnectionStatus()

    init {
        viewModelScope.launch {
            try {
                if (internetNoAccess) {
                    Log.d(LOG_TAG, "There is no Internet access, local data will be downloaded")
                    pagination = false
                    withContext(IO) {
                        if (filmsRepository.isCacheEmpty()) {
                            Log.d(
                                LOG_TAG,
                                "The cache is empty, data will be loaded from the local JSON"
                            )
                            isProgressBarVisibleMutableLiveData.postValue(true)
                            myList.addAll(resProvider.loadFilms())
                        } else {
                            Log.d(LOG_TAG, "Loading data from a database")
                            myList.addAll(filmsRepository.getListOfFilmsFromCache())
                        }
                        _items.postValue(myList)
                    }
                } else {
                    Log.d(LOG_TAG, "There is  Internet access")
                    withContext(IO) {
                        if (!filmsRepository.isCacheEmpty()) {
                            Log.d(LOG_TAG, "Loading data from the cache")
                            myList.addAll(filmsRepository.getListOfFilmsFromCache())
                            _items.postValue(myList)
                        }
                    }
                    Log.d(LOG_TAG, "Downloading data from the network")
                    myList.clear()
                    myList.addAll(filmsRepository.getListOfFilms(page))
                    _items.postValue(myList)
                    Log.d(LOG_TAG, "Saving data to the cache")
                    filmsRepository.clearCache()
                    filmsRepository.fillCache(myList)
                }
            } catch (error: Throwable) {
                _error.value = ERROR_LOAD_MOVIES
            } finally {
                isProgressBarVisibleMutableLiveData.postValue(false)
            }
        }
    }

    fun getSearchMovie(query: String) {
        pagination = false
        viewModelScope.launch {
            try {
                isProgressBarVisibleMutableLiveData.value = true
                myList.clear()
                myList.addAll(filmsRepository.getListOfFilms2(query))
                _items.value = myList
            } catch (error: Throwable) {
                _error.value = ERROR_LOAD_MOVIES
            } finally {
                isProgressBarVisibleMutableLiveData.value = false
            }
        }
    }

    fun getDefaultList() {
        page = 1
        pagination = true
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

    fun getListOfMovieFromUserDatabase() {
        pagination = false
        viewModelScope.launch {
            try {
                isProgressBarVisibleMutableLiveData.value = true
                myList.clear()
                myList.addAll(filmsRepository.getListOfFilmsFromUserDatabase())
                _items.value = myList
            } catch (error: Throwable) {
                _error.value = ERROR_LOAD_MOVIES
            } finally {
                isProgressBarVisibleMutableLiveData.value = false
            }
        }
    }

    fun addNextListOfMovies() {
        if (pagination) {
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