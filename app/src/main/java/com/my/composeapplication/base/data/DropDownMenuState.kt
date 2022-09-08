package com.my.composeapplication.base.data

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * Created by YourName on 2022/09/08.
 */
data class DropDownMenuState(
    var menuList : List<String>,
    var isShow : Boolean = false,
    var anchor : DpOffset = DpOffset(0.dp, 0.dp),
)
