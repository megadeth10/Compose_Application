package com.my.composeapplication.scene.health.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * Created by YourName on 2022/08/22.
 */
class TodoItem(
    var id : Int,
    var text : String,
    initChecked : Boolean = false,
) {
    var isChecked by mutableStateOf<Boolean>(initChecked)
}
