package com.my.composeapplication.viewmodel

import android.util.Log
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
class InfinityListViewModel @Inject constructor() : ViewModel() {
    init {
        Log.e(InfinityListViewModel::class.simpleName, "init() this: $this")
    }

    private var isProgress : Boolean = false
    private var _list : SnapshotStateList<TodoItem> = SnapshotStateList()
    val list : List<TodoItem> get() = _list

    private var _isRefreshing : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshing : StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private var _horizontalPagerItems = ArrayList<PagerItem>(arrayListOf())
    val horizontalPagerItems : ArrayList<PagerItem> get() = _horizontalPagerItems

    private val saveStataParams = hashMapOf<String, Any>()

    fun saveStateParam(key : String, param : Any) {
        this.saveStataParams[key] = param
    }

    fun restoreStateParam(key : String) = this.saveStataParams.remove(key)

    fun setRefresh(newState : Boolean) {
        if (this.isRefreshing.value != newState) {
            viewModelScope.launch {
                this@InfinityListViewModel._isRefreshing.emit(newState)
            }
        }
        Log.e(InfinityListViewModel::class.java.simpleName, "onRefresh() value: ${this.isRefreshing.value}")
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

    fun setExpend(item : TodoItem, state : Boolean) {
        allExpend()
        _list.find { it.id == item.id }?.let {
            if (it.isExpend != state) {
                it.isExpend = state
            }
        }
    }

    private fun allExpend() {
        _list.forEach {
            if (it.isExpend) {
                it.isExpend = false
            }
        }
    }

    private fun setProgress(newState : Boolean) {
        if (isProgress != newState) {
            isProgress = newState
        }
    }

    var random = false
    private fun setPagerItem() {
        if (random) {
            this._horizontalPagerItems = getInitPager()
            random = false
        } else {
            this._horizontalPagerItems = getInitPager2()
            random = true
        }

    }

    fun moreList() {
        if (isProgress) {
            Log.e(InfinityListViewModel::class.java.simpleName, "moreList() return end")
            return
        }
        this.setProgress(true)
        Log.e(InfinityListViewModel::class.java.simpleName, "moreList()")
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
        Log.e(InfinityListViewModel::class.java.simpleName, "appendItem()")
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

private fun getInitList(startIndex : Int = 0) = List(20) { i ->
    val id = i + startIndex
    val title = "Todo Item ${i + startIndex}"
    val description = "$id\n$title              \n$title            \n$title "
    TodoItem(id, title, description)
}

private fun getInitPager() : ArrayList<PagerItem> = arrayListOf<PagerItem>(
    PagerItem("https://cdn.pixabay.com/photo/2020/07/14/16/18/snow-5404785_960_720.jpg", " 산이다."),
    PagerItem("https://cdn.pixabay.com/photo/2022/08/19/10/35/scooter-7396608_960_720.jpg", " 스쿠터 자전거"),
    PagerItem("https://cdn.pixabay.com/photo/2022/08/17/20/42/hot-air-balloon-7393437_960_720.jpg", " 열기구 카파도피아"),
    PagerItem("https://cdn.pixabay.com/photo/2022/07/30/04/46/sunrise-7353034_960_720.jpg", " 바다 석양"),
    PagerItem("https://cdn.pixabay.com/photo/2022/07/30/14/35/sunflowers-7353922_960_720.jpg", " 해바라기"),
    PagerItem("https://cdn.pixabay.com/photo/2022/08/18/11/29/wind-energy-7394705_960_720.jpg", " 풍력 발전기"),
)

private fun getInitPager2(): ArrayList<PagerItem> = arrayListOf<PagerItem>(
    PagerItem("https://cdn.pixabay.com/photo/2022/03/01/09/35/iceland-poppy-7040946_960_720.jpg", " 양귀비"),
    PagerItem("https://cdn.pixabay.com/photo/2013/07/21/13/00/rose-165819_960_720.jpg", " 장미"),
    PagerItem("https://cdn.pixabay.com/photo/2014/04/14/20/11/pink-324175_960_720.jpg", "벗꽃"),
    PagerItem("https://cdn.pixabay.com/photo/2015/10/09/00/55/lotus-978659_960_720.jpg", " 연꽃"),
    PagerItem("https://cdn.pixabay.com/photo/2014/02/27/16/10/flowers-276014_960_720.jpg", " 야생화"),
    PagerItem("https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510_960_720.jpg", " 데이지"),
)