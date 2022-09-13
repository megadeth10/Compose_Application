package com.my.composeapplication.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.composeapplication.R
import com.my.composeapplication.base.data.SwitchGroupState

/**
 * Created by YourName on 2022/09/13.
 * Switch group compose
 */

@Composable
fun SwitchGroupHoisting(
    modifier : Modifier = Modifier,
    switchGroupState : SwitchGroupState<String>,
    onCheckChange : (List<String>) -> Unit = {}
) {
    var switchState by remember {
        mutableStateOf(switchGroupState)
    }

    SwitchGroupCompose(
        modifier = modifier,
        state = switchState,
        onCheckChange = {
            switchState = switchState.copy(
                checkedItems = it
            )
            onCheckChange(it)
        }
    )
}

@Composable
fun SwitchGroupCompose(
    modifier : Modifier = Modifier,
    state : SwitchGroupState<String>,
    onCheckChange : (List<String>) -> Unit = {}
) {
    val onClick : (String) -> Unit = { selectedTitle ->
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
            SwitchTextCompose(
                modifier = modifier,
                title = item,
                onClick = onClick,
                checkedItem = state.checkedItems
            )
        }
    }
}

@Composable
fun SwitchTextCompose(
    modifier : Modifier = Modifier,
    title : String,
    onClick : (String) -> Unit,
    checkedItem : List<String>,
) {
    Row(
        modifier = modifier
            .clickable {
                onClick.invoke(title)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checkedItem.any { it == title },
            onCheckedChange = null
        )
    }
}

@Preview(name = "SwitchGroup")
@Composable
fun SwitchGroupPreview() {
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    SwitchGroupCompose(
        state = SwitchGroupState(
            itemList = list.toList()
        )
    )
}


