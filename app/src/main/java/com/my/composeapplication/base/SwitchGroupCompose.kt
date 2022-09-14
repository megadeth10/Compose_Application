package com.my.composeapplication.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.my.composeapplication.R
import com.my.composeapplication.base.data.SwitchGroupState

/**
 * Created by YourName on 2022/09/13.
 * Switch group compose
 */
private val paddingSize = 5.dp
@Composable
fun SwitchGroupHoisting(
    modifier : Modifier = Modifier,
    switchGroupState : SwitchGroupState<String>,
    onCheckChange : (List<String>) -> Unit = {},
    reversLayout : Boolean = false
) {
    var switchState by remember {
        mutableStateOf(switchGroupState)
    }

    SwitchGroupCompose(
        modifier = modifier,
        state = switchState,
        onCheckChange = {
            switchState = SwitchGroupState(
                itemList = switchState.itemList,
                checkedItems = it
            )
            onCheckChange(it)
        },
        reversLayout = reversLayout
    )
}

@Composable
private fun SwitchGroupCompose(
    modifier : Modifier = Modifier,
    state : SwitchGroupState<String>,
    onCheckChange : (List<String>) -> Unit = {},
    reversLayout : Boolean = false
) {
    GroupItemCompose(
        modifier = modifier,
        state = state,
        onCheckChange = onCheckChange,
    ) { item, onClick ->
        SwitchTextCompose(
            modifier = modifier,
            title = item,
            onClick = onClick,
            checkedItem = state.checkedItems,
            reversLayout = reversLayout
        )
    }
}

@Composable
private fun SwitchTextCompose(
    modifier : Modifier = Modifier,
    title : String,
    onClick : (String) -> Unit,
    checkedItem : List<String>,
    reversLayout : Boolean = false
) {
    ConstraintLayout(
        modifier = modifier
            .clickable {
                onClick.invoke(title)
            }
            .padding(paddingSize)
            .fillMaxWidth()
    ) {
        val (checkbox, text) = createRefs()
        Text(
            text = title,
            modifier = Modifier
                .constrainAs(text) {
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                    var startLink = checkbox.end
                    var endLink = parent.end
                    var startPadding = paddingSize
                    if (reversLayout) {
                        startLink = parent.start
                        endLink = checkbox.start
                        startPadding = 0.dp
                    }

                    start.linkTo(startLink, startPadding)
                    end.linkTo(endLink)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        Switch(
            modifier = Modifier
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
            checked = checkedItem.any { it == title },
            onCheckedChange = null,
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

@Preview(name = "SwitchRevGroup")
@Composable
fun SwitchGroupRevPreview() {
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    SwitchGroupCompose(
        state = SwitchGroupState(
            itemList = list.toList()
        ),
        reversLayout = true
    )
}