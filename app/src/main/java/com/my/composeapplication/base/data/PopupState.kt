package com.my.composeapplication.base.data

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset

/**
 * Created by YourName on 2022/09/07.
 */
data class PopupState(
    var isShow : Boolean = false,
    @Deprecated(message = "PopupView는 anchor 대신에 해당 Compose를 wrapping해야한다.")
    var anchor: IntOffset = IntOffset(0,0),
    var content: String = "",
    var alignment : Alignment = Alignment.BottomCenter
)
