package com.my.composeapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Created by YourName on 2022/06/27.
 */
class MainViewModel : ViewModel() {
    private var _data : MutableLiveData<Boolean?> = MutableLiveData(null)
    val data get() = _data

    init {
        Log.e(MainViewModel::class.simpleName, "MainViewModel init() $this")
    }

    fun setData(newState : Boolean) {
        viewModelScope.launch {
            if (newState != this@MainViewModel.data.value) {
                this@MainViewModel._data.value = newState
            }
        }
    }

    override fun onCleared() {
        Log.e(MainViewModel::class.simpleName, "MainViewModel onCleared() $this")
        super.onCleared()
    }
}