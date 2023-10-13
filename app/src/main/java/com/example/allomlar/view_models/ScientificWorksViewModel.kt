package com.mac.allomalar.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mac.allomalar.models.Book
import com.mac.allomalar.models.ResourceList
import com.mac.allomalar.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScientificWorksViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    suspend fun getAllBooksFromRoom(allomaId: Int) =
        repository.getAllBooksOfAllomaFromRoom(allomaId)

    private val _allBooks = MutableLiveData<ResourceList<Book>>()
    val allBooks: LiveData<ResourceList<Book>>
        get() = _allBooks

    init {
        getAllBooks()
    }

    suspend fun insertAllBooks(list: List<Book?>?) = repository.insertBooks(list)

    private fun getAllBooks() {
        viewModelScope.launch {
            _allBooks.postValue(ResourceList.loading(null))
            try {
                repository.getAllBooks().let {
                    if (it.isSuccessful) {
                        _allBooks.postValue(ResourceList.success(it.body()))
                    } else {
                        _allBooks.postValue(ResourceList.error(it.message(), null))
                    }
                }
            } catch (e: Exception) {
                Log.d("TAG", "getAllBooks: ScientificWorksViewModel")
            }
        }
    }

}