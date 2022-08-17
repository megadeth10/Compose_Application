package com.my.composeapplication.viewmodel

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
    private val _height : MutableLiveData<String> = MutableLiveData("")
    val height : LiveData<String> = _height

    private val _weight : MutableLiveData<String> = MutableLiveData("")
    val weight : LiveData<String> = _weight

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