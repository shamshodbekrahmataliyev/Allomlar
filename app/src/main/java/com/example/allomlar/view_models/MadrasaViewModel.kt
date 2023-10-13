package com.mac.allomalar.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mac.allomalar.models.Alloma
import com.mac.allomalar.models.MadrasaAndAllomas
import com.mac.allomalar.models.Resource
import com.mac.allomalar.models.ResourceList
import com.mac.allomalar.repository.Repository
import com.mac.allomalar.ui.activities.AllomalarActivity
import com.mac.allomalar.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MadrasaViewModel @Inject constructor(
    private val repository: Repository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val _madrasaAndAllomasLiveData = MutableLiveData<ResourceList<MadrasaAndAllomas>>()
    val madrasaAndAllomasLiveData: LiveData<ResourceList<MadrasaAndAllomas>>
        get() = _madrasaAndAllomasLiveData

    suspend fun getAllMadrasaAndAllomas() = repository.getAllMadrasaAndAllomasFromRoom()
    suspend fun insertMadrasaAndAllomas(list: List<MadrasaAndAllomas?>?) =
        repository.insertMadrasaAndAllomasAll(list)

    init {
        if (networkHelper.isNetworkConnected())
            getMadrasaAndAllomas()
    }

    private fun getMadrasaAndAllomas() {
        try {
            viewModelScope.launch {
                _madrasaAndAllomasLiveData.postValue(ResourceList.loading(null))
               try {
                   repository.getMadrasaAndAllomas().let {
                       if (it.isSuccessful) {
                           _madrasaAndAllomasLiveData.postValue(ResourceList.success(it.body()))
                       } else {
                           _madrasaAndAllomasLiveData.postValue(
                               ResourceList.error(
                                   it.errorBody().toString(),
                                   null
                               )
                           )

                       }
                   }
               }catch (e: Exception){
                   Log.d("TAG", "getMadrasaAndAllomas: MAdrasaViewModel")
               }
            }
        } catch (e: Exception) {
            Log.d("TAG", "getMadrasaAndAllomas: MANDA")
        }
    }
}