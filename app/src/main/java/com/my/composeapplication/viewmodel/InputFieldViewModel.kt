package com.my.composeapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.my.composeapplication.base.BaseAlertViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.cancel
import javax.inject.Inject

/**
 * Created by YourName on 2022/08/30.
 */
@HiltViewModel
class InputFieldViewModel @Inject constructor() : BaseAlertViewModel() {
    // 입력 delay 전송 Job
    private val inputWatchJob = Job() + Dispatchers.IO
    private var inputDelayJob : Job? = null
    private val _inputFlow = MutableStateFlow<String>("")
    private val _inputValue = mutableStateOf<String>("")
    val inputValue : State<String> get() = this._inputValue

    init {
        viewModelScope.launch(inputWatchJob) {
            this@InputFieldViewModel._inputFlow.collect {
                Log.e("LEE", "_inputSnapshot text: $it")
            }
        }
    }

    fun changeInput(text : String) {
        this._inputValue.value = text
        inputDelayJob?.cancel()
        inputDelayJob = this.viewModelScope.launch {
            delay(300)
            _inputFlow.emit(_inputValue.value)
        }
    }

    override fun onCleared() {
        Log.e("LEE", "onCleared()")
        this.inputDelayJob?.job?.cancel()
        this.inputWatchJob.job.cancel()
        this.inputWatchJob.cancel()
        super.onCleared()
    }
}