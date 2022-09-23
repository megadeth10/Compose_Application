package com.my.composeapplication.base

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.my.composeapplication.viewmodel.InfinityListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 네트워크 동작 관련 기본 객체
 */
abstract class BaseNetworkViewModel: BaseAlertViewModel() {
    /**
     * Rest Api 상태용
     */
    private var _isProgress : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isProgress : StateFlow<Boolean> get() = _isProgress.asStateFlow()

    /**
     * 목록 갱신상태용
     */
    private var _isRefreshing : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing : StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    /**
     * state의 상태값 저장용도
     */
    private val saveStataParams = hashMapOf<String, Any>()

    //상태값 저장
    fun saveStateParam(key : String, param : Any) {
        this.saveStataParams[key] = param
    }

    /**
     * 상태값 읽기 - 주의!! 읽기는 읽음과 동시에 삭제 된다.
     */
    fun restoreStateParam(key : String) = this.saveStataParams.remove(key)

    /**
     * 갱신 상태값 갱신
     */
    fun setRefresh(newState : Boolean) {
        if (this.isRefreshing.value != newState) {
            viewModelScope.launch {
                this@BaseNetworkViewModel._isRefreshing.emit(newState)
            }
        }
        Log.e(InfinityListViewModel::class.java.simpleName, "onRefresh() value: ${this.isRefreshing.value}")
    }

    /**
     * 통신 진행 상태값 갱신
     */
    protected fun setProgress(newState : Boolean) {
        if (this.isProgress.value != newState) {
            viewModelScope.launch {
                this@BaseNetworkViewModel._isProgress.emit(newState)
            }
        }
    }

    abstract fun onRefresh()
}