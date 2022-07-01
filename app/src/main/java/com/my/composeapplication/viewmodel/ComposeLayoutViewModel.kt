package com.my.composeapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Created by YourName on 2022/06/30.
 */
class ComposeLayoutViewModel : ViewModel() {
    private val _mutableMenuId : MutableLiveData<Int> = MutableLiveData<Int>(0)
    val mutableMenuId get() = _mutableMenuId
    private var _menuId : Int = 0
    val menuId get() = _menuId

    fun setMutableMenuId(newState : Int) {
        viewModelScope.launch {
            if (this@ComposeLayoutViewModel._mutableMenuId.value != newState) {
                this@ComposeLayoutViewModel._mutableMenuId.value = newState
            }
        }
    }

    fun setMenuId(newState : Int) {
        viewModelScope.launch {
            if (this@ComposeLayoutViewModel._menuId != newState) {
                this@ComposeLayoutViewModel._menuId = newState
            }
        }
    }
}