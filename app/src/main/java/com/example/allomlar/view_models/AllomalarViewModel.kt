package com.mac.allomalar.view_models

import androidx.lifecycle.ViewModel
import com.mac.allomalar.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllomalarViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    suspend fun getAllCenturiesFromRoom() = repository.getAllCenturiesFromRoom()
}