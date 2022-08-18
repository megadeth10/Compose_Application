package com.my.composeapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by YourName on 2022/08/16.
 */
@HiltViewModel
class BMIViewModel @Inject constructor() : ViewModel() {
    private val _height = mutableStateOf<String>("")
    val height : State<String> = _height

    private val _weight = mutableStateOf<String>("")
    val weight : State<String> = _weight


    init {
        Log.e("BMIViewModel", "init() this: $this")
    }
    /**
     * 키 설정
     */
    fun setHeight(newHeight : String) {
        this._height.value = newHeight
    }

    /**
     * 몸무게 설정
     */
    fun setWeight(newWeight : String) {
        this._weight.value = newWeight
    }
}