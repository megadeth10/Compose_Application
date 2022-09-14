package com.my.composeapplication.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.my.composeapplication.base.data.PopupState

/**
 * Created by YourName on 2022/09/08.
 * Popup 생성을 위한 CustomCompose
 */

private val paddingSize = 5.dp

/**
 * 중요 포인터 PopupView의 Anchor가 없는 이유는 Box로 감싸기만 하면 알아서 표시 된다.
 */
@Composable
fun PopupViewCompose(
    modifier : Modifier = Modifier,
    popupState : PopupState,
    content : @Composable (popupCallback : (PopupState?) -> Unit) -> Unit
) {
    var popupShow by remember {
        mutableStateOf(popupState)
    }
    val showCallback : (PopupState?) -> Unit = {
        val newPopupShow = it ?: popupShow.copy()
        newPopupShow.isShow = true
        popupShow = newPopupShow
    }
    Box(modifier) {
        content(showCallback)
        PopupView(
            popupState = popupShow,
            onClose = {
                val popup = popupShow.copy(isShow = false)
                popupShow = popup
            }
        )
    }
}

@Composable
fun PopupView(
    modifier : Modifier = Modifier,
    popupState : PopupState,
    onClose : () -> Unit
) {
    if (popupState.isShow) {
        Popup(
            alignment = popupState.alignment,
            onDismissRequest = {
                onClose()
            },
            offset = popupState.anchor
        ) {
            PopupContentView(popupState.content)
        }
    }
}

@Composable
fun PopupContentView(content : String) {
    Text(
        text = content,
        modifier = Modifier
            .background(Color.Gray, RoundedCornerShape(paddingSize))
            .border(1.dp, Color.Black, shape = RoundedCornerShape(paddingSize))
            .padding(paddingSize),
    )
}