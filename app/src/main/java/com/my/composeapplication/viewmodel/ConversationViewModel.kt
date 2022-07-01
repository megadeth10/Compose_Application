package com.my.composeapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.my.composeapplication.data.Message

/**
 * Created by YourName on 2022/06/29.
 */
class ConversationViewModel(messages : List<Message>) : ViewModel() {
    private var _list = MutableLiveData<List<Message>>(messages)
    val list get() = _list

    init {
        Log.e(ConversationViewModel::class.simpleName, "init() $this")
    }
    fun setMessage(index : Int, msg : Message) {

    }
}