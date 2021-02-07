package com.android_academy.chartal_application.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_academy.chartal_application.data.Actor
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.repository.FilmsRepository
import com.android_academy.chartal_application.util.NetworkStatus
import com.android_academy.chartal_application.util.SingleLiveEvent
import kotlinx.coroutines.launch


private const val ERROR_LOAD_TRAILER = "Error load trailer"
private const val ERROR_LOAD_ACTORS = "Error load actors"

class DetailsViewModel(
    private val filmsRepository: FilmsRepository,
    private val networkStatus: NetworkStatus
) : ViewModel() {

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> get() = _actors

    private val _trailerUrl = MutableLiveData<String>()
    val trailerUrl: LiveData<String> get() = _trailerUrl

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String> get() = _error

    private val internetAccess: Boolean
        get() = networkStatus.internetConnectionStatus()

    fun loadActors(id: Int) {
        if (internetAccess) {
            viewModelScope.launch {
                try {
                    _actors.value = filmsRepository.getActorsAndSaveInCache(id)
                } catch (error: Throwable) {
                    _error.value = ERROR_LOAD_ACTORS
                }
            }
        } else {
            loadActorsFromCache(id)
        }
    }

    fun loadActorsFromCache(id: Int) {
        viewModelScope.launch {
            try {
                _actors.value = filmsRepository.getActorsFromCache(id)?.take(8)
            } catch (error: Throwable) {
                _error.value = ERROR_LOAD_ACTORS
            }
        }
    }

    fun getTrailer(id: Int) {
        viewModelScope.launch {
            try {
                _trailerUrl.value = filmsRepository.getTrailer(id)
            } catch (error: Throwable) {
                _error.value = ERROR_LOAD_TRAILER
            }
        }
    }

    fun saveUserMovie(movie: Movie?) {
        viewModelScope.launch {
            filmsRepository.saveUserMovie(movie)
        }
    }

    fun deleteUserMovie(movie: Movie?) {
        viewModelScope.launch {
            filmsRepository.deleteUserMovie(movie)
        }
    }

}