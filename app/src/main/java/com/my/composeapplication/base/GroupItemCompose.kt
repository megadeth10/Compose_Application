package com.my.composeapplication.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.my.composeapplication.base.data.BaseGroupState

/**
 * Created by YourName on 2022/09/13.
 * Group Item Compose
 */
private val paddingSize = 5.dp

@Composable
fun GroupItemCompose(
    modifier : Modifier = Modifier,
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
        state.itemList.forEachIndexed { index, item ->
            childCompose(item, localOnClick)
            if (state.itemList.size > index + 1) {
                Divider(modifier = modifier.padding(horizontal = paddingSize), color = Color.Black)
            }
        }
    }
}