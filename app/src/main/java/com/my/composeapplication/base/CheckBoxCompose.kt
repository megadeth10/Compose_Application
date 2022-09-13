package com.my.composeapplication.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.composeapplication.R
import com.my.composeapplication.base.data.CheckGroupState

/**
 * Created by YourName on 2022/09/13.
 * Checkbox Group Compose
 */

@Composable
fun CheckboxGroupWithAllHoisting(
    modifier : Modifier = Modifier,
    groupTitle : String = "",
    isCheckAll : Boolean = false,
    checkGroupState : CheckGroupState<String>,
    onCheckedChange : (List<String>) -> Unit = {},
    onCheckedAllChange : (Boolean) -> Unit = {}
) {
    var allCheck by remember {
        mutableStateOf(isCheckAll)
    }
    var checkList by remember {
        mutableStateOf(checkGroupState)
    }
    CheckboxGroupWithAllCompose(
        modifier = modifier,
        groupTitle = groupTitle,
        isCheckAll = allCheck,
        checkGroupState = checkList,
        onCheckedChange = {
            checkList = checkList.copy(
                checkedItems = it
            )
            onCheckedChange(it)
        },
        onCheckedAllChange = {
            allCheck = it
            onCheckedAllChange(it)
        }
    )
}

@Composable
fun CheckboxGroupWithAllCompose(
    modifier : Modifier = Modifier,
    groupTitle : String = "",
    isCheckAll : Boolean = false,
    checkGroupState : CheckGroupState<String>,
    onCheckedChange : (List<String>) -> Unit = {},
    onCheckedAllChange : (Boolean) -> Unit = {}
) {
    val checkedChange : (List<String>) -> Unit = {
        val isAll = it.size == checkGroupState.itemList.size
        onCheckedAllChange(isAll)
        onCheckedChange(it)
    }
    val allChange : (Boolean) -> Unit = {
        val selectedList = if (it) {
            checkGroupState.itemList.toList()
        } else {
            listOf()
        }
        onCheckedChange(selectedList)
        onCheckedAllChange(it)
    }
    Column {
        CheckGroupTitle(
            modifier = modifier,
            title = groupTitle,
            selected = isCheckAll,
            onClick = {
                allChange(it)
            }
        )
        CheckboxGroupCompose(
            modifier = modifier,
            checkGroupState = checkGroupState,
            onCheckedChange = checkedChange,
        )
    }
}

@Composable
fun CheckboxGroupHoisting(
    modifier : Modifier = Modifier,
    checkGroupState : CheckGroupState<String>,
    onCheckedChange : (List<String>) -> Unit = {}
) {
    var checkState by remember {
        mutableStateOf(checkGroupState)
    }

    CheckboxGroupCompose(
        modifier = modifier,
        checkGroupState = checkState,
        setCheckState = {
            checkState = it
        },
        onCheckedChange = onCheckedChange
    )
}

@Composable
fun CheckboxGroupCompose(
    modifier : Modifier = Modifier,
    checkGroupState : CheckGroupState<String>,
    setCheckState : (CheckGroupState<String>) -> Unit = {},
    onCheckedChange : (List<String>) -> Unit = {}
) {
    val onClick : (String) -> Unit = { selectedTitle ->
        val hasItem = checkGroupState.checkedItems.find { it == selectedTitle }
        val list = if (hasItem?.isNotEmpty() == true) {
            checkGroupState.checkedItems.minus(selectedTitle)
        } else {
            checkGroupState.checkedItems.plus(selectedTitle)
        }
        setCheckState(
            checkGroupState.copy(
                checkedItems = list
            )
        )
        onCheckedChange(list)
    }

    Column {
        checkGroupState.itemList.forEach {
            CheckBoxTextCompose(
                modifier = modifier,
                title = it,
                onClick = onClick,
                selected = checkGroupState.checkedItems
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

@Composable
fun CheckGroupTitle(
    modifier : Modifier = Modifier,
    title : String,
    onClick : (Boolean) -> Unit,
    selected : Boolean
) {
    Row(
        modifier = modifier
            .clickable {
                onClick.invoke(!selected)
            }
            .height(30.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                modifier = Modifier.weight(1f)
            )
        }
        Checkbox(
            modifier = Modifier
                .size(30.dp),
            checked = selected,
            onCheckedChange = null,
        )
    }
}

@Preview(name = "CheckBoxGroup")
@Composable
fun CheckBoxGroupPreview() {
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    CheckboxGroupCompose(
        checkGroupState = CheckGroupState(
            itemList = list.toList()
        )
    )
}

@Preview(name = "CheckTitleGroup")
@Composable
fun CheckTitleGroupPreview() {
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    CheckboxGroupWithAllCompose(
        checkGroupState = CheckGroupState(
            itemList = list.toList()
        ),
        groupTitle = "Aaa"
    )
}