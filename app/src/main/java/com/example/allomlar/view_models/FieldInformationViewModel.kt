package com.mac.allomalar.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mac.allomalar.models.Resource
import com.mac.allomalar.models.ResourceList
import com.mac.allomalar.models.Subject
import com.mac.allomalar.models.SubjectInfo
import com.mac.allomalar.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FieldInformationViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _allSubjectsInformation = MutableLiveData<ResourceList<SubjectInfo>>()
    val allSubjectsInfo: LiveData<ResourceList<SubjectInfo>>
        get() = _allSubjectsInformation

    private val _allSubjectsInformationInside = MutableLiveData<ResourceList<SubjectInfo>>()
    private val allSubjectsInside: LiveData<ResourceList<SubjectInfo>>
        get() = _allSubjectsInformationInside

    private val subjectList = ArrayList<SubjectInfo>()


    suspend fun getAllSubjectsInfo(subjectId: Int) = repository.getAllSubjectInfoFromRoom( subjectId)
    suspend fun getAllomaById(allomaId: Int) = repository.getAllomaFromRoom(allomaId)
    suspend fun getImageById(allomaImage: String) = repository.getImageFromRoomById(allomaImage)
    suspend fun insertSubjectInfo(list: List<SubjectInfo?>?) = repository.insertSubjectInfoAll(list)

    fun getAllSubjectsInside(i: Int) {
        viewModelScope.launch {
            _allSubjectsInformationInside.postValue(ResourceList.loading(null))
                try {
                    repository.getAllSubjectsInfo(i).let {
                        if (it.isSuccessful) {
                            _allSubjectsInformationInside.postValue(ResourceList.success(it.body()))
                            it.body()?.forEach {
                                subjectList.add(it)
                            }
                        } else {
                            _allSubjectsInformationInside.postValue(
                                ResourceList.error(
                                    it.errorBody().toString(), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "getAllSubjectInfo: Scholar 2")
                }
            _allSubjectsInformation.postValue(ResourceList.success(subjectList))
        }
    }
}