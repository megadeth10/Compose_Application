package com.my.composeapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.composeapplication.scene.health.data.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    fun moreList() {
        if (isProgress) {
            return
        }
        this.setProgress(true)
        Log.e(HealthViewModel::class.java.simpleName, "moreList()")
        viewModelScope.launch(Dispatchers.Unconfined) {
            delay(2000)
            withContext(Dispatchers.Main) {
                appendItem()
            }
        }
    }

    private fun appendItem() {
        Log.e(HealthViewModel::class.java.simpleName, "appendItem()")
        val currentList = this._list
        currentList.addAll(getInitList(currentList.size))
        this._list = currentList
        this.setProgress(false)
    }
}

private fun getInitList(startIndex:Int = 0) = List(20) { i -> TodoItem(i+startIndex, "Todo Item ${i + startIndex}") }