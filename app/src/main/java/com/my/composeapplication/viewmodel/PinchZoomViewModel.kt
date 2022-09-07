package com.my.composeapplication.viewmodel

import android.util.Size
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.animatePanBy
import androidx.compose.foundation.gestures.panBy
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import com.my.composeapplication.base.BaseAlertViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by YourName on 2022/09/05.
 */
@HiltViewModel
class PinchZoomViewModel @Inject constructor() : BaseAlertViewModel() {
    var offset = Offset.Zero

    var zoom = 1f

    var angle = 0f

    private val _size = mutableStateOf(Size(0, 0))
    val size : State<Size> get() = this._size

    fun setSize(size : Size) {
        this._size.value = size
    }

    fun storeValue(
        zoom : Float, offset : Offset, angle : Float
    ) {
        this.offset = offset
        this.zoom = zoom
        this.angle = angle
    }
}