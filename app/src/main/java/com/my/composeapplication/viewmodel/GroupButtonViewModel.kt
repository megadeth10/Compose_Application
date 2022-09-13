package com.my.composeapplication.viewmodel

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

    fun setCheckSelected(list : List<String>) {
        this._checkBoxSelected = list
    }

    fun setRadioSelected(list : List<String>) {
        this._selected = list
    }
}
