package com.mac.allomalar.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mac.allomalar.models.Madrasa
import com.mac.allomalar.models.ResourceList
import com.mac.allomalar.repository.Repository
import com.mac.allomalar.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagerFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _allMadrasasLiveData = MutableLiveData<ResourceList<Madrasa>>()
    val allMadrasas: LiveData<ResourceList<Madrasa>>
        get() = _allMadrasasLiveData

    init {
        if (networkHelper.isNetworkConnected()) {
            readAllMadrasa()
        }
    }

    suspend fun getAllMadrasaFromRoom() = repository.getAllMadrasasFromRoom()

    private fun readAllMadrasa() = viewModelScope.launch {
        _allMadrasasLiveData.postValue(ResourceList.loading(null))
        repository.getAllMadrasas().let { respond ->
            if (respond.isSuccessful) {
                _allMadrasasLiveData.postValue(ResourceList.success(respond.body()))
            } else {
                _allMadrasasLiveData.postValue(
                    ResourceList.error(
                        respond.errorBody().toString(),
                        null
                    )
                )
            }
        }
    }
}