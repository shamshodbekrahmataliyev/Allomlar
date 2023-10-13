package com.mac.allomalar.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mac.allomalar.models.Century
import com.mac.allomalar.models.Madrasa
import com.mac.allomalar.models.ResourceList
import com.mac.allomalar.repository.Repository
import com.mac.allomalar.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val networkHelper: NetworkHelper
) : ViewModel() {


    private val _centuryLiveData = MutableLiveData<ResourceList<Century>>()
    val allCenturies: LiveData<ResourceList<Century>>
        get() = _centuryLiveData

    private val _allMadrasasLiveData = MutableLiveData<ResourceList<Madrasa>>()
    val allMadrasas: LiveData<ResourceList<Madrasa>>
        get() = _allMadrasasLiveData

    init {
        if (networkHelper.isNetworkConnected()) {
            getCenturies()
            readAllMadrasa()
        }
    }

    suspend fun getAllCenturiesFromRoom() = repository.getAllCenturiesFromRoom()
    suspend fun insertAllCenturies(list: List<Century?>?) = repository.insertAllData(list)
    suspend fun insertAllMadrasa(list: List<Madrasa?>?) = repository.insertMadrasas(list)


    private fun getCenturies() = viewModelScope.launch {
        _centuryLiveData.postValue(ResourceList.loading(null))

        try {
            repository.getAllCenturies().let {
                if (it.isSuccessful) {
                    _centuryLiveData.postValue(ResourceList.success(it.body()))
                } else {
                    _centuryLiveData.postValue(ResourceList.error(it.errorBody().toString(), null))
                }
            }
        } catch (e: Exception) {
            Log.d("TAG", "getCenturies: HomeViewModel")
        }
    }

    private fun readAllMadrasa() {
        viewModelScope.launch {
            _allMadrasasLiveData.postValue(ResourceList.loading(null))
            try {
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
            } catch (e: Exception) {
                Log.d("TAG", "readAllMadrasa: HomeViewModel")
            }
        }

    }

}
