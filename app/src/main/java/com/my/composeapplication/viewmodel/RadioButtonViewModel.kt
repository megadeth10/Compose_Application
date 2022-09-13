package com.my.composeapplication.viewmodel

import com.my.composeapplication.base.BaseAlertViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by YourName on 2022/09/08.
 */
@HiltViewModel
class RadioButtonViewModel @Inject constructor() : BaseAlertViewModel() {
    /**
     * SnackBar State
     */
    private var _selected : List<String> = listOf()
    val selected : List<String> get() = this._selected

    fun setSelected(list : List<String>) {
        this._selected = list
    }
}