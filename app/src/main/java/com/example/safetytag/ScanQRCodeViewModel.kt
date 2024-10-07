package com.example.safetytag

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akash.mybarcodescanner.data.repo.MainRepoImpl
import com.akash.mybarcodescanner.presentation.MainScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScanQRCodeViewModel:ViewModel() {
    val repo = MainRepoImpl()
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()


    fun startScanning(context: Context){
        viewModelScope.launch {
            repo.startScanning(context).collect{
                if (!it.isNullOrBlank()){
                    _state.value = state.value.copy(
                        details = it
                    )
                }
            }
        }
    }
}