package com.my.composeapplication.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.my.composeapplication.base.data.CheckGroupState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.composeapplication.R

/**
 * Created by YourName on 2022/09/13.
 * Checkbox Group Compose
 */

@Composable
fun CheckBoxGroupCompose(
    modifier : Modifier = Modifier,
    checkGroupState : CheckGroupState<String>,
    onCheckedChange : (List<String>) -> Unit = {}
) {
    var checkState by remember {
        mutableStateOf(checkGroupState)
    }

    val onClick : (String) -> Unit = { selectedTitle ->
        val hasItem = checkState.checkedItems.find { it == selectedTitle }
        val list = if (hasItem?.isNotEmpty() == true) {
            checkState.checkedItems.minus(selectedTitle)
        } else {
            checkState.checkedItems.plus(selectedTitle)
        }
        checkState = checkState.copy(
            checkedItems = list
        )
        onCheckedChange(checkState.checkedItems)
    }

    Column {
        checkState.itemList.forEach {
            CheckBoxTextCompose(
                modifier = modifier,
                title = it,
                onClick = onClick,
                selected = checkState.checkedItems
            )
        }
    }
}

@Composable
fun CheckBoxTextCompose(
    modifier : Modifier = Modifier,
    title : String,
    onClick : (String) -> Unit,
    selected : List<String>
) {
    Row(
        modifier = modifier
            .clickable {
                onClick.invoke(title)
            }
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            modifier = Modifier.size(30.dp),
            checked = selected.any { it == title },
            onCheckedChange = null
        )
        Text(
            text = title,
            modifier = Modifier.wrapContentWidth()
        )
    }
}

@Preview(name = "CheckBoxGroup")
@Composable
fun CheckBoxGroupPreview() {
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    CheckBoxGroupCompose(
        checkGroupState = CheckGroupState(
            itemList = list.toList()
        )
    )
}