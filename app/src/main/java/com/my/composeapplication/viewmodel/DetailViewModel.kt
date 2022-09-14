package com.my.composeapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.my.composeapplication.base.BaseAlertViewModel
import com.my.composeapplication.scene.health.data.PagerItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Created by YourName on 2022/09/14.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(): BaseAlertViewModel() {
    private var waitJob : Job? = null
    private var _isRefreshing : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing : StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    val _horizontalPagerItems = listOf<PagerItem>(
        PagerItem("https://cdn.pixabay.com/photo/2020/07/14/16/18/snow-5404785_960_720.jpg", " 산이다."),
        PagerItem("https://cdn.pixabay.com/photo/2022/08/19/10/35/scooter-7396608_960_720.jpg", " 스쿠터 자전거"),
        PagerItem("https://cdn.pixabay.com/photo/2022/08/17/20/42/hot-air-balloon-7393437_960_720.jpg", " 열기구 카파도피아"),
        PagerItem("https://cdn.pixabay.com/photo/2022/07/30/04/46/sunrise-7353034_960_720.jpg", " 바다 석양"),
        PagerItem("https://cdn.pixabay.com/photo/2022/07/30/14/35/sunflowers-7353922_960_720.jpg", " 해바라기"),
        PagerItem("https://cdn.pixabay.com/photo/2022/08/18/11/29/wind-energy-7394705_960_720.jpg", " 풍력 발전기"),
    )

    fun setRefresh(newState : Boolean) {
        if (this.isRefreshing.value != newState) {
            viewModelScope.launch {
                this@DetailViewModel._isRefreshing.emit(newState)
            }
        }
        Log.e(HealthViewModel::class.java.simpleName, "onRefresh() value: ${this.isRefreshing.value}")
    }

    fun onRefresh() {
        if (this.isRefreshing.value) {
            return
        }
        this.setRefresh(true)
        waitJob = viewModelScope.launch(Dispatchers.IO) {
            refreshWaiting()
        }
    }

    private suspend fun refreshWaiting() {
        delay(2000)
        withContext(Dispatchers.Main) {
            this@DetailViewModel.setRefresh(false)
            waitJob = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        waitJob?.cancel()
    }
}