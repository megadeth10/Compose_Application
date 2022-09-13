package com.my.composeapplication.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.my.composeapplication.R
import com.my.composeapplication.base.data.RadioGroupState

/**
 * Created by YourName on 2022/09/08.
 * RadioButton Custom Compose
 */

@Composable
fun RadioGroupHoisting(
    modifier : Modifier = Modifier,
    radioGroupState : RadioGroupState<String>,
    onChangeSelected : (List<String>) -> Unit = {},
    reversLayout : Boolean = false
) {
    var radioState by remember {
        mutableStateOf(radioGroupState)
    }
    RadioGroupCompose(
        modifier = modifier,
        radioGroupState = radioState,
        onChangeSelected = {
            val newState = RadioGroupState.copy(radioState)
            newState.checkedItems = it
            radioState = newState
            onChangeSelected(it)
        },
        reversLayout = reversLayout
    )
}

@Composable
private fun RadioGroupCompose(
    modifier : Modifier = Modifier,
    radioGroupState : RadioGroupState<String>,
    onChangeSelected : (List<String>) -> Unit = {},
    reversLayout : Boolean = false
) {
    val localOnClick : (String) -> Unit = { selectedTitle ->
        val currentList = if (radioGroupState.isMulti) {
            val hasItem = radioGroupState.checkedItems.find { title ->
                selectedTitle == title
            }
            val newRadioButtonState = RadioGroupState.copy(radioGroupState)
            if (hasItem?.isNotEmpty() == true) {
                newRadioButtonState.checkedItems.minus(selectedTitle)
            } else {
                newRadioButtonState.checkedItems.plus(selectedTitle)
            }
        } else {
            listOf(selectedTitle)
        }
        onChangeSelected(currentList)
    }

    GroupItemCompose(
        modifier = modifier,
        state = radioGroupState,
        onCheckChange = onChangeSelected,
        onClick = localOnClick
    ) { item, onClick ->
        RadioButtonTextView(
            modifier = modifier,
            title = item,
            onClick = onClick,
            selected = radioGroupState.checkedItems,
            reversLayout = reversLayout
        )
    }
}

@Composable
private fun RadioButtonTextView(
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
        RadioButton(
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
            selected = selected.any { it == title },
            onClick = null
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


@Preview(name = "RadioGroup")
@Composable
fun RadioGroupPreview() {
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    RadioGroupHoisting(
        radioGroupState = RadioGroupState(
            itemList = list.toList(),
        ),
        modifier = Modifier.width(200.dp)
    )
}
