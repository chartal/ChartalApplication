package com.android_academy.chartal_application.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_academy.chartal_application.data.Actor
import com.android_academy.chartal_application.repository.FilmsRepository
import com.android_academy.chartal_application.repository.NetworkModule
import kotlinx.coroutines.launch


private const val ERROR_LOAD_TRAILER = "Error load trailer"
private const val ERROR_LOAD_ACTORS = "Error load actors"

class DetailsViewModel(private val filmsRepository: FilmsRepository) : ViewModel() {

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> get() = _actors

    private val _trailerUrlMutableLiveData = MutableLiveData<String>()
    val trailerUrlMutableLiveData: LiveData<String> get() = _trailerUrlMutableLiveData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadActors(id: Int) {
        viewModelScope.launch {
            try {
                _actors.value = filmsRepository.getActors(id).take(10)
            }catch(error: Throwable){
                _error.value = ERROR_LOAD_ACTORS
            }
        }
    }

    fun getTrailer(id: Int) {
        viewModelScope.launch {
            try {
                _trailerUrlMutableLiveData.value = filmsRepository.getTrailer(id)
            } catch (error: Throwable) {
                _error.value = ERROR_LOAD_TRAILER
            }
        }
    }
}