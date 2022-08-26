package com.my.composeapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.composeapplication.scene.health.data.PagerItem
import com.my.composeapplication.scene.health.data.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by YourName on 2022/08/22.
 */
@HiltViewModel
class HealthViewModel @Inject constructor() : ViewModel() {
    init {
        Log.e(HealthViewModel::class.simpleName, "init() this: $this")
    }

    private var isProgress : Boolean = false
    private var _list : SnapshotStateList<TodoItem> = SnapshotStateList()
    val list : List<TodoItem> get() = _list

    private var _isRefreshing : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing : StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private var _horizontalPagerItems = mutableStateListOf<PagerItem>()
    val horizontalPagerItems: List<PagerItem> get() = _horizontalPagerItems

    private val saveStataParams = hashMapOf<String, Any>()

    fun saveStateParam(key:String, param:Any) {
        this.saveStataParams[key] = param
    }

    fun restoreStateParam(key:String) = this.saveStataParams.remove(key)

    fun setRefresh(newState : Boolean) {
        if (this.isRefreshing.value != newState) {
            viewModelScope.launch {
                this@HealthViewModel._isRefreshing.emit(newState)
            }
        }
        Log.e(HealthViewModel::class.java.simpleName, "onRefresh() value: ${this.isRefreshing.value}")
    }

    fun setCheck(item : TodoItem, state : Boolean) = _list.find { it.id == item.id }?.let {
        it.isChecked = state
    }

    fun removeItem(item : TodoItem) = _list.remove(item)

    fun allCheck(standardState : Boolean) = _list.forEach {
        if (it.isChecked != standardState) {
            it.isChecked = standardState
        }
    }

    private fun setProgress(newState : Boolean) {
        if (isProgress != newState) {
            isProgress = newState
        }
    }

    private fun setPagerItem() {
        val currentItem = this._horizontalPagerItems
        currentItem.addAll(getInitPager())
    }

    fun moreList() {
        if (isProgress) {
            return
        }
        this.setProgress(true)
        Log.e(HealthViewModel::class.java.simpleName, "moreList()")
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            withContext(Dispatchers.Main) {
                appendItem()
                checkPagerItem()
            }
        }
    }

    private fun checkPagerItem() {
        if (this.horizontalPagerItems.isEmpty()) {
            setPagerItem()
        }
    }

    private fun appendItem() {
        Log.e(HealthViewModel::class.java.simpleName, "appendItem()")
        val currentList = this._list
        currentList.addAll(getInitList(currentList.size))
        this._list = currentList
        this.setProgress(false)
        this.setRefresh(false)
    }

    fun onRefresh() {
        if (this.isRefreshing.value) {
            return
        }
        this.setRefresh(true)
        _list.clear()
        this._horizontalPagerItems.clear()
        moreList()
    }
}

private fun getInitList(startIndex : Int = 0) = List(20) { i -> TodoItem(i + startIndex, "Todo Item ${i + startIndex}") }
private fun getInitPager() = listOf<PagerItem>(
    PagerItem("https://cdn.pixabay.com/photo/2020/07/14/16/18/snow-5404785_960_720.jpg", " 산이다."),
    PagerItem("https://cdn.pixabay.com/photo/2022/08/19/10/35/scooter-7396608_960_720.jpg", " 스쿠터 자전거"),
    PagerItem("https://cdn.pixabay.com/photo/2022/08/17/20/42/hot-air-balloon-7393437_960_720.jpg", " 열기구 카파도피아"),
    PagerItem("https://cdn.pixabay.com/photo/2022/07/30/04/46/sunrise-7353034_960_720.jpg", " 바다 석양"),
    PagerItem("https://cdn.pixabay.com/photo/2022/07/30/14/35/sunflowers-7353922_960_720.jpg", " 해바라기"),
    PagerItem("https://cdn.pixabay.com/photo/2022/08/18/11/29/wind-energy-7394705_960_720.jpg", " 풍력 발전기"),
)