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
fun RadioGroupView(
    modifier : Modifier = Modifier,
    radioGroupState : RadioGroupState<String>,
    onChangeSelected : (List<String>) -> Unit = {},
) {
    var radioButtonState by remember {
        mutableStateOf(radioGroupState)
    }
    val onClick : (String) -> Unit = { selectedTitle ->
        radioButtonState = if (radioButtonState.isMulti) {
            val hasItem = radioButtonState.selected.find { title ->
                selectedTitle == title
            }
            val newRadioButtonState = radioButtonState.copy()
            if (hasItem?.isNotEmpty() == true) {
                newRadioButtonState.selected = newRadioButtonState.selected.minus(selectedTitle)
                newRadioButtonState
            } else {
                newRadioButtonState.selected = newRadioButtonState.selected.plus(selectedTitle)
                newRadioButtonState
            }
        } else {
            val newRadioButtonState = radioButtonState.copy(selected = listOf(selectedTitle))
            newRadioButtonState
        }
        onChangeSelected(radioButtonState.selected)
    }
    Column {
        radioButtonState.menuList.forEach {
            RadioButtonTextView(
                modifier = modifier,
                title = it,
                onClick = onClick,
                selected = radioButtonState.selected
            )
        }
    }
}

@Composable
fun RadioButtonTextView(
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
    RadioGroupView(
        radioGroupState = RadioGroupState(
            menuList = list.toList(),
        ),
        modifier = Modifier.width(200.dp)
    )
}
