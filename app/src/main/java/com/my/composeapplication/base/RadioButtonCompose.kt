package com.my.composeapplication.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.composeapplication.base.data.RadioGroupState
import com.my.composeapplication.R

/**
 * Created by YourName on 2022/09/08.
 * RadioButton Custom Compose
 */

@Composable
fun RadioGroupHoisting(
    modifier : Modifier = Modifier,
    radioGroupState : RadioGroupState<String>,
    onChangeSelected : (List<String>) -> Unit = {},
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
        }
    )
}

@Composable
private fun RadioGroupCompose(
    modifier : Modifier = Modifier,
    radioGroupState : RadioGroupState<String>,
    onChangeSelected : (List<String>) -> Unit = {},
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
            selected = radioGroupState.checkedItems
        )
    }
}

@Composable
private fun RadioButtonTextView(
    modifier : Modifier = Modifier,
    title : String,
    onClick : (String) -> Unit,
    selected : List<String>
) {
    Row(
        modifier = modifier
            .clickable {
                onClick.invoke(title)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.size(30.dp),
            selected = selected.any { it == title },
            onClick = null
        )
        Text(
            text = title,
            modifier = Modifier.wrapContentWidth()
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
