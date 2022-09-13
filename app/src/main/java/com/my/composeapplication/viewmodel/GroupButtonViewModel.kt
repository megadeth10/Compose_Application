package com.my.composeapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.my.composeapplication.base.BaseAlertViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by YourName on 2022/09/08.
 */
@HiltViewModel
class GroupButtonViewModel @Inject constructor() : BaseAlertViewModel() {
    private var _selected : List<String> = listOf()
    val selected : List<String> get() = this._selected

    private var _checkBoxSelected : List<String> = listOf()
    val checkBoxSelected : List<String> get() = this._checkBoxSelected

//    private var _checkBoxAllSelected : MutableState<Boolean> = mutableStateOf(false)
//    val checkBoxAllSelected : State<Boolean> get() = this._checkBoxAllSelected
    var checkBoxAllSelected : Boolean = false
        private set

    fun setCheckAll(newState: Boolean) {
        Log.e("LEE", "setCheckAll() state: $newState")
//        this._checkBoxAllSelected.value = newState
        this.checkBoxAllSelected = newState
    }

    fun setCheckSelected(list : List<String>) {
        Log.e("LEE", "setCheckSelected() list: $list")
        this._checkBoxSelected = list
    }

    fun setRadioSelected(list : List<String>) {
        this._selected = list
    }
}
