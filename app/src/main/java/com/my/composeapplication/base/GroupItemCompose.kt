package com.my.composeapplication.base

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.my.composeapplication.base.data.BaseGroupState

/**
 * Created by YourName on 2022/09/13.
 * Group Item Compose
 */

@Composable
fun GroupItemCompose(
    state : BaseGroupState<String>,
    onClick : ((String) -> Unit)? = null,
    onCheckChange : (List<String>) -> Unit = {},
    childCompose : @Composable (
        String,
        (String) -> Unit
    ) -> Unit
) {
    val localOnClick : (String) -> Unit = onClick ?: { selectedTitle ->
        val hasItem = state.checkedItems.find { it == selectedTitle }
        val list = if (hasItem?.isNotEmpty() == true) {
            state.checkedItems.minus(selectedTitle)
        } else {
            state.checkedItems.plus(selectedTitle)
        }
        onCheckChange(list)
    }
    Column {
        state.itemList.forEach { item ->
            childCompose(item, localOnClick)
        }
    }
}