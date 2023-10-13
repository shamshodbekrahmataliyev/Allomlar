package com.mac.allomalar.view_models

import androidx.lifecycle.ViewModel
import com.mac.allomalar.repository.Repository
import com.mac.allomalar.utils.NetworkStateChangeReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Scholar1ViewModel @Inject constructor(
     val repository: Repository
): ViewModel(){
    suspend fun getAlloma(id: Int) = repository.getAllomaFromRoom(id)
}