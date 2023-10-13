package com.mac.allomalar.view_models

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mac.allomalar.models.*
import com.mac.allomalar.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val TAG = "SplashViewModel"
    private val _allMadrasasLiveData = MutableLiveData<ResourceList<Madrasa>>()
    val allMadrasas: LiveData<ResourceList<Madrasa>>
        get() = _allMadrasasLiveData

    private val _centuryLiveData = MutableLiveData<ResourceList<Century>>()
    val allCenturies: LiveData<ResourceList<Century>>
        get() = _centuryLiveData

    private val _madrasaAndAllomasLiveData = MutableLiveData<ResourceList<MadrasaAndAllomas>>()
    val madrasaAndAllomasLiveData: LiveData<ResourceList<MadrasaAndAllomas>>
        get() = _madrasaAndAllomasLiveData

    private val _allAllomasInside = MutableLiveData<Resource<Alloma>>()
    private val allAllomasInside: LiveData<Resource<Alloma>>
        get() = _allAllomasInside

    private val _allAllomas = MutableLiveData<ResourceList<Alloma>>()
    val allAllomas: LiveData<ResourceList<Alloma>>
        get() = _allAllomas

    private val _allSubjects = MutableLiveData<ResourceList<Subject>>()
    val allSubjects: LiveData<ResourceList<Subject>>
        get() = _allSubjects

    private val _allSubjectsInside = MutableLiveData<ResourceList<Subject>>()
    private val allSubjectsInside: LiveData<ResourceList<Subject>>
        get() = _allSubjectsInside

    private val _allBooks = MutableLiveData<ResourceList<Book>>()
    val allBooks : LiveData<ResourceList<Book>>
        get() = _allBooks

    private val _allSciences = MutableLiveData<ResourceList<Science>>()
    val allScience : LiveData<ResourceList<Science>>
        get() = _allSciences

    private val allomaList = ArrayList<Alloma>()
    private val subjectList = ArrayList<Subject>()

    init {
        getCenturies()
        readAllMadrasa()
        getMadrasaAndAllomas()
        getEachAllomasInside()
        getAllSubjects()
        getAllBooks()
        getAllSciences()
    }


    suspend fun insertAllCenturies(list: List<Century?>?) = repository.insertAllData(list)
    suspend fun insertAllMadrasa(list: List<Madrasa?>?) = repository.insertMadrasas(list)
    suspend fun insertMadrasaAndAllomas(list: List<MadrasaAndAllomas?>?) = repository.insertMadrasaAndAllomasAll(list)
    suspend fun insertAllomas(list: List<Alloma?>?) = repository.insertAllomas(list)
    suspend fun insertSubjects(list: List<Subject?>?) = repository.insertSubjectsAll(list)
    suspend fun insertAllBooks(list: List<Book?>?) = repository.insertBooks(list)
    suspend fun insertAllSciences(list: List<Science?>?) = repository.insertSciences(list)

    private fun getAllSciences() {
      try {
          viewModelScope.launch {
              _allSciences.postValue(ResourceList.loading(null))

              repository.getAllScience().let {
                  if (it.isSuccessful) {
                      _allSciences.postValue(ResourceList.success(it.body()))
                  } else {
                      _allSciences.postValue(ResourceList.error(it.message(), null))
                  }
              }
          }
      }catch (e: Exception){
          Log.d(TAG, " Exception")
      }
    }

    private fun getAllBooks() {
        try {
        viewModelScope.launch {
            _allBooks.postValue(ResourceList.loading(null))

            repository.getAllBooks().let {
                if (it.isSuccessful) {
                    _allBooks.postValue(ResourceList.success(it.body()))
                } else {
                    _allBooks.postValue(ResourceList.error(it.message(), null))
                }
            }
        }
        }catch (e: Exception){
            Log.d(TAG, "getAllBooks: Exception Books")
        }

    }
    private fun getAllSubjects() {
       try {
           viewModelScope.launch {
               var isShouldDo = true
               var i = 1
               while (isShouldDo) {
                   _allSubjectsInside.postValue(ResourceList.loading(null))
                   repository.getAllSubjectsOfAlloma(i).let {
                       if (it.isSuccessful) {
                           _allSubjectsInside.postValue(ResourceList.success(it.body()))
                           it.body()?.forEach {
                               it.allomaId = i
                               subjectList.add(it)
                           }
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
               }
               _allSubjects.postValue(ResourceList.success(subjectList))
           }
       }catch (e: Exception){
           Log.d(TAG, "getAllSubjects: Subject")
       }
    }
    private fun getEachAllomasInside() {
       try {
           viewModelScope.launch {
               var isShouldDo = true
               var i = 1
               while (isShouldDo) {
                   _allAllomasInside.postValue(Resource.loading(null))
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
               }
               _allAllomas.postValue(ResourceList.success(allomaList))
           }
       }catch (e: Exception){
           Log.d(TAG, "getEachAllomasInside: Alloma")
       }
    }


    private fun getMadrasaAndAllomas() {
        try {
            viewModelScope.launch {
                _madrasaAndAllomasLiveData.postValue(ResourceList.loading(null))
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
            }
        }catch (e: Exception){
            Log.d(TAG, "getMadrasaAndAllomas: MANDA")
        }
    }


    private fun getCenturies() {
       try {
           viewModelScope.launch {
               _centuryLiveData.postValue(ResourceList.loading(null))
               repository.getAllCenturies().let {
                   if (it.isSuccessful) {
                       _centuryLiveData.postValue(ResourceList.success(it.body()))
                   } else {
                       _centuryLiveData.postValue(
                           ResourceList.error(
                               it.errorBody().toString(),
                               null
                           )
                       )
                   }
               }
           }
       }catch (e: Exception){
           Log.d(TAG, "getCenturies: century")
       }
    }

    private fun readAllMadrasa() {
        try {
            viewModelScope.launch {
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
        }catch (e: Exception){
            Log.d(TAG, "readAllMadrasa: madrasas")
        }

    }
}