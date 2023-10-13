package com.mac.allomalar.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mac.allomalar.models.Alloma
import com.mac.allomalar.models.Resource
import com.mac.allomalar.models.ResourceList
import com.mac.allomalar.repository.Repository
import com.mac.allomalar.ui.activities.AllomalarActivity
import com.mac.allomalar.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: Repository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _allAllomasInside = MutableLiveData<Resource<Alloma>>()
    private val allAllomasInside: LiveData<Resource<Alloma>>
        get() = _allAllomasInside

    private val _allAllomas = MutableLiveData<ResourceList<Alloma>>()
    val allAllomas: LiveData<ResourceList<Alloma>>
        get() = _allAllomas

    private val allomaList = ArrayList<Alloma>()

    init {
        if (networkHelper.isNetworkConnected() && !AllomalarActivity.isAllomasReadFromApi)
            getEachAllomasInside()
    }

    suspend fun insertAllomas(list: List<Alloma?>?) = repository.insertAllomas(list)
    suspend fun getAllAllomasFromRoom() = repository.getAllAllomasFromRoom()

    private fun getEachAllomasInside() {
        viewModelScope.launch {
            var isShouldDo = true
            var i = 1
            while (isShouldDo) {
                _allAllomasInside.postValue(Resource.loading(null))
                try {
                    repository.getEachAlloma(i).let {
                        if (it.isSuccessful) {
                            _allAllomasInside.postValue(Resource.success(it.body()))
                            allomaList.add(it.body()!!)
                            i++
                        } else {
                            _allAllomasInside.postValue(
                                Resource.error(
                                    it.errorBody().toString(),
                                    null
                                )
                            )
                            isShouldDo = false
                        }
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "getEachAllomasInside:${e.message}")
                }
            }
            _allAllomas.postValue(ResourceList.success(allomaList))
        }
    }
}