package com.my.composeapplication.base.data

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset

/**
 * Created by YourName on 2022/09/07.
 */
data class PopupState(
    var isShow : Boolean = false,
    var anchor: IntOffset = IntOffset(0,0),
    var content: String = "",
    var alignment : Alignment = Alignment.BottomCenter
)
