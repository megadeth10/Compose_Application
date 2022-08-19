package com.my.composeapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.my.composeapplication.base.ScrollStateParam
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by YourName on 2022/08/19.
 */
@HiltViewModel
class LayoutComposeViewModel @Inject constructor(): ViewModel() {
    private val scrollStateMap = HashMap<String, ScrollStateParam>()

    fun setScrollState(key:String, param : ScrollStateParam){
        this.scrollStateMap[key] = param
    }

    fun getScrollState(key:String) = this.scrollStateMap.remove(key)
}