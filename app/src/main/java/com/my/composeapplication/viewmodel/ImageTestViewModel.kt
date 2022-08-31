package com.my.composeapplication.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.my.composeapplication.base.BaseAlertViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by YourName on 2022/08/31.
 */
@HiltViewModel
class ImageTestViewModel @Inject constructor() : BaseAlertViewModel() {
    private val _imageData: MutableState<Any?> = mutableStateOf(null)
    val imageData : State<Any?> get() = this._imageData

    fun setImageData(data : Any?) {
        this._imageData.value = data
    }
}