package com.mac.allomalar.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mac.allomalar.models.ResourceList
import com.mac.allomalar.models.Science
import com.mac.allomalar.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorldFondViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _allSciences = MutableLiveData<ResourceList<Science>>()
    val allScience: LiveData<ResourceList<Science>>
        get() = _allSciences

    init {
        getAllSciences()
    }

    suspend fun getAllScienceFromRoom(allomaId: Int) =
        repository.getAllScienceOfAllomaFromRoom(allomaId)

    suspend fun getAllomaById(allomaId: Int) = repository.getAllomaFromRoom(allomaId)
    suspend fun getImageById(imageUrl: String) = repository.getImageFromRoomById(imageUrl)
    suspend fun insertAllSciences(list: List<Science?>?) = repository.insertSciences(list)

    private fun getAllSciences() {
        viewModelScope.launch {
            _allSciences.postValue(ResourceList.loading(null))
            try {
                repository.getAllScience().let {
                    if (it.isSuccessful) {
                        _allSciences.postValue(ResourceList.success(it.body()))
                    } else {
                        _allSciences.postValue(ResourceList.error(it.message(), null))
                    }
                }
            }catch (e: Exception){
                Log.d("TAG", "getAllSciences: ")
            }            }

        }
    }