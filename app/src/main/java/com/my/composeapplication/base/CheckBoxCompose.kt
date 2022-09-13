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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
    onCheckedAllChange : (Boolean) -> Unit = {},
    reversLayout : Boolean = false
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
            checkList = CheckGroupState(
                itemList = checkList.itemList,
                checkedItems = it
            )
            onCheckedChange(it)
        },
        onCheckedAllChange = {
            allCheck = it
            onCheckedAllChange(it)
        },
        reversLayout = reversLayout
    )
}

@Composable
private fun CheckboxGroupWithAllCompose(
    modifier : Modifier = Modifier,
    groupTitle : String = "",
    isCheckAll : Boolean = false,
    checkGroupState : CheckGroupState<String>,
    onCheckedChange : (List<String>) -> Unit = {},
    onCheckedAllChange : (Boolean) -> Unit = {},
    reversLayout : Boolean = false
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
            reversLayout = reversLayout
        )
    }
}

@Composable
fun CheckboxGroupHoisting(
    modifier : Modifier = Modifier,
    checkGroupState : CheckGroupState<String>,
    onCheckedChange : (List<String>) -> Unit = {},
    reversLayout : Boolean = false
) {
    var checkState by remember {
        mutableStateOf(checkGroupState)
    }

    CheckboxGroupCompose(
        modifier = modifier,
        checkGroupState = checkState,
        onCheckedChange = {
            checkState = CheckGroupState(
                itemList = checkState.itemList,
                checkedItems = it
            )
            onCheckedChange(it)
        },
        reversLayout = reversLayout
    )
}

@Composable
private fun CheckboxGroupCompose(
    modifier : Modifier = Modifier,
    checkGroupState : CheckGroupState<String>,
    onCheckedChange : (List<String>) -> Unit = {},
    reversLayout : Boolean = false
) {
    GroupItemCompose(
        modifier = modifier,
        state = checkGroupState,
        onCheckChange = onCheckedChange,
    ) { item, onClick ->
        CheckBoxTextCompose(
            modifier = modifier,
            title = item,
            onClick = onClick,
            selected = checkGroupState.checkedItems,
            reversLayout = reversLayout
        )
    }
}

@Composable
private fun CheckBoxTextCompose(
    modifier : Modifier = Modifier,
    title : String,
    onClick : (String) -> Unit,
    selected : List<String>,
    reversLayout : Boolean = false
) {
    ConstraintLayout(
        modifier = modifier
            .clickable {
                onClick.invoke(title)
            }
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        val (checkbox, text) = createRefs()
        Checkbox(
            modifier = Modifier
                .size(30.dp)
                .constrainAs(checkbox) {
                    var startLink = parent.start
                    var endLink = text.start
                    if (reversLayout) {
                        startLink = text.end
                        endLink = parent.end
                    }

                    start.linkTo(startLink)
                    end.linkTo(endLink)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            checked = selected.any { it == title },
            onCheckedChange = null
        )
        Text(
            text = title,
            modifier = Modifier
                .constrainAs(text) {
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                    var startLink = checkbox.end
                    var endLink = parent.end
                    if (reversLayout) {
                        startLink = parent.start
                        endLink = checkbox.start
                    }

                    start.linkTo(startLink)
                    end.linkTo(endLink)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
private fun CheckGroupTitle(
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
            .padding(5.dp)
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

@Preview(name = "CheckBoxGroupReverse")
@Composable
fun CheckBoxGroupRevPreview() {
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    CheckboxGroupCompose(
        checkGroupState = CheckGroupState(
            itemList = list.toList()
        ),
        reversLayout = true
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