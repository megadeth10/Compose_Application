package com.my.composeapplication.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.my.composeapplication.base.data.DropDownMenuState

/**
 * Created by YourName on 2022/09/08.
 * DropdownMenu Compose
 * ref : https://foso.github.io/Jetpack-Compose-Playground/material/dropdownmenu/
 */


@Composable
fun DropDownMenuCompose(
    modifier : Modifier = Modifier,
    menuModifier : Modifier = Modifier,
    dropDownMenuState : DropDownMenuState,
    selectedItem : String? = null,
    onMenuClick : (String) -> Unit = {},
    content : @Composable (popupCallback : (DropDownMenuState?) -> Unit, isShow : Boolean) -> Unit
) {
    var dropDownShow by remember {
        mutableStateOf(dropDownMenuState)
    }
    val showCallback : (DropDownMenuState?) -> Unit = { state ->
        val newDropDownShow = state ?: dropDownShow.copy()
        newDropDownShow.isShow = true
        dropDownShow = newDropDownShow
    }
    Box(modifier) {
        content(showCallback, dropDownShow.isShow)
        DropdownMenu(
            expanded = dropDownShow.isShow,
            onDismissRequest = {
                val popup = dropDownShow.copy(isShow = false)
                dropDownShow = popup
            },
            offset = dropDownMenuState.anchor,
        ) {
            dropDownMenuState.menuList.forEach { title ->
                DropdownMenuItem(
                    onClick = {
                        val newDropDownShow = dropDownShow.copy()
                        newDropDownShow.isShow = false
                        dropDownShow = newDropDownShow
                        onMenuClick.invoke(title)
                    },
                    modifier = menuModifier.background(
                        if (selectedItem == title) {
                            Color.LightGray
                        } else {
                            Color.White
                        }
                    ),
                ) {
                    Text(
                        text = title,
                    )
                }
            }
        }
    }
}