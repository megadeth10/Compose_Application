package com.my.composeapplication.scene.health.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * Created by YourName on 2022/08/22.
 */
class TodoItem(
    val id : Int,
    val text : String,
    val description : String,
    initChecked : Boolean = false,
    initExpend : Boolean = false,
) {
    var isChecked by mutableStateOf<Boolean>(initChecked)
    var isExpend by mutableStateOf<Boolean>(initExpend)
}
