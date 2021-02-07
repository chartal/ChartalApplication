package com.android_academy.chartal_application.details

import android.util.Log
import androidx.lifecycle.*
import com.android_academy.chartal_application.App
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.data.UserMovie
import com.android_academy.chartal_application.repository.FilmsRepository
import com.android_academy.chartal_application.room.AppDatabase
import com.android_academy.chartal_application.util.IResProvider
import com.android_academy.chartal_application.util.NetworkStatus
import com.android_academy.chartal_application.util.SingleLiveEvent
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

    private val movies = MutableLiveData<List<Movie>>()

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String> get() = _error

    private val isProgressBarVisibleMutableLiveData = SingleLiveEvent<Boolean>()
    val isProgressBarVisible: LiveData<Boolean> = isProgressBarVisibleMutableLiveData

    val isUserFilmsTableEmpty: LiveData<Int> = AppDatabase.getDatabase(App.instance).filmUserDao().isUserFilmsTableEmpty()

    val userMovies: LiveData<List<UserMovie>> =
        AppDatabase.getDatabase(App.instance).filmUserDao().getAllExperiment()

    private val moviesFromDatabase: LiveData<List<Movie>> =
        AppDatabase.getDatabase(App.instance).filmDao().getAll()

    val moviesMediatorLiveData: MediatorLiveData<List<Movie>> = MediatorLiveData()

    private var page: Int = 1
    private val myList = mutableListOf<Movie>()
    private var pagination = true
    private val internetNoAccess: Boolean
        get() = !networkStatus.internetConnectionStatus()

    init {
        moviesMediatorLiveData.addSource(movies, Observer {
            moviesMediatorLiveData.value = it
        })
        moviesMediatorLiveData.addSource(moviesFromDatabase, Observer {
            moviesMediatorLiveData.value = it
        })
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
                            filmsRepository.fillCache(resProvider.loadFilms())
                        }
                    }
                } else {
                    Log.d(LOG_TAG, "There is  Internet access")
                    if (!filmsRepository.isCacheEmpty()) {
                        myList.addAll(filmsRepository.getListOfFilmsFromCache())
                    }
                    filmsRepository.fillCache(filmsRepository.getListOfFilms(page))
                    Log.d(LOG_TAG, "The cache has been updated")
                }
            } catch (error: Throwable) {
                _error.value = ERROR_LOAD_MOVIES
            } finally {
                isProgressBarVisibleMutableLiveData.postValue(false)
            }
        }
    }

    fun getSearchMovies(query: String) {
        pagination = false
        viewModelScope.launch {
            try {
                isProgressBarVisibleMutableLiveData.value = true
                myList.clear()
                myList.addAll(filmsRepository.getSearchMovies(query))
                movies.value = myList
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
                movies.value = myList
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
                movies.value = myList
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
                        movies.value = myList
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