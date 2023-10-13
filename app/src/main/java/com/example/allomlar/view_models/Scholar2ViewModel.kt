package com.mac.allomalar.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mac.allomalar.models.Resource
import com.mac.allomalar.models.ResourceList
import com.mac.allomalar.models.Subject
import com.mac.allomalar.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Scholar2ViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _allSubjects = MutableLiveData<ResourceList<Subject>>()
    val allSubjects: LiveData<ResourceList<Subject>>
        get() = _allSubjects

    private val _allSubjectsInside = MutableLiveData<ResourceList<Subject>>()
    private val allSubjectsInside: LiveData<ResourceList<Subject>>
        get() = _allSubjectsInside

    private val subjectList = ArrayList<Subject>()


    suspend fun getAllSubjects(allomaId: Int) = repository.getAllSubjectsFromRoom(allomaId)
    suspend fun getAllomaById(allomaId: Int) = repository.getAllomaFromRoom(allomaId)
    suspend fun getImageById(allomaImage: String) = repository.getImageFromRoomById(allomaImage)
    suspend fun insertSubjects(list: List<Subject?>?) = repository.insertSubjectsAll(list)

    fun getAllSubjectsInside(i: Int) {
        viewModelScope.launch {
             _allSubjectsInside.postValue(ResourceList.loading(null))
                try {
                    repository.getAllSubjectsOfAlloma(i).let {
                        if (it.isSuccessful) {
                            _allSubjectsInside.postValue(ResourceList.success(it.body()))
                            it.body()?.forEach {
                                it.allomaId = i
                                subjectList.add(it)
                            }
                        } else {
                            _allSubjectsInside.postValue(
                                ResourceList.error(
                                    it.errorBody().toString(), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "getAllSubjects: Scholar 2")
                }
                _allSubjects.postValue(ResourceList.success(subjectList))

        }
    }
}