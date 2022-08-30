package com.my.composeapplication.base

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.my.composeapplication.base.data.ChoiceDialogState
import com.my.composeapplication.base.data.DialogState
import com.my.composeapplication.base.data.DialogType
import com.my.composeapplication.scene.DialogActivity

/**
 * Created by YourName on 2022/08/29.
 */
@Composable
fun ChoiceDialog(
    modifier : Modifier = Modifier,
    dialogState : ChoiceDialogState,
    onDismiss : () -> Unit
) {
    dialogState.list?.filterIsInstance<String>()?.let { list ->
        var selected by remember {
            mutableStateOf("")
        }
        val onClick : (String) -> Unit = { title ->
            selected = title
        }
        Column(
            modifier = modifier
                .padding(10.dp)
        ) {
            list.forEach { title ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clickable { onClick(title) }
                        .padding(vertical = 5.dp),
                    verticalAlignment = CenterVertically
                ) {
                    RadioButton(
                        modifier = Modifier,
                        selected = selected == title,
                        onClick = null,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth(),
                        maxLines = 1,
                    )
                }

            }
        }
        DialogButtonRow(
            negativeButtonText = dialogState.negativeButtonText,
            onNegativeClick = dialogState.onNegativeClick,
            positiveButtonText = dialogState.positiveButtonText,
            onPositiveClick = {
                dialogState.onPositiveClick?.invoke(selected)
            },
            onDismiss = onDismiss
        )
    } ?: Text(
        text = "ChoiceDialog는 List의 type이 String 이어야 한다."
    )
}

@Composable
fun DefaultDialog(
    modifier : Modifier = Modifier,
    dialogState : DialogState,
    onDismiss : () -> Unit,
) {
    if (dialogState.isShow) {
        Dialog(
            onDismissRequest = { onDismiss() },
        ) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Box(
                modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    when (dialogState.useChildComposable) {
                        DialogType.ChoiceDialog -> {
                            ChoiceDialog(
                                modifier = Modifier.fillMaxWidth(),
                                dialogState = dialogState as ChoiceDialogState,
                                onDismiss = onDismiss
                            )
                        }
                        else -> {
                            SimpleDialogLayout(
                                dialogState = dialogState,
                                onDismiss = onDismiss
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleDialogLayout(
    modifier : Modifier = Modifier,
    dialogState : DialogState,
    onDismiss : () -> Unit,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 20.dp),
        text = dialogState.message,
        color = MaterialTheme.colorScheme.secondary
    )
    DialogButtonRow(
        negativeButtonText = dialogState.negativeButtonText,
        onNegativeClick = dialogState.onNegativeClick,
        positiveButtonText = dialogState.positiveButtonText,
        onPositiveClick = dialogState.onPositiveClick,
        onDismiss = onDismiss
    )
}

@Composable
fun DialogButtonRow(
    negativeButtonText : String? = null,
    onNegativeClick : (() -> Unit)? = null,
    positiveButtonText : String = "",
    onPositiveClick : ((Any?) -> Unit)? = null,
    onDismiss : () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
    ) {
        negativeButtonText?.let {
            DialogButton(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Gray),
                buttonText = it,
                onClick = {
                    onDismiss()
                    onNegativeClick?.invoke()
                }
            )
        }
        DialogButton(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary),
            buttonText = positiveButtonText,
            onClick = {
                onPositiveClick?.invoke(null) ?: onDismiss()
            }
        )
    }
}

@Composable
fun DialogButton(
    modifier : Modifier = Modifier,
    buttonText : String,
    onClick : () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Text(
            text = buttonText,
            color = MaterialTheme.colorScheme.surface
        )
    }
}

@Preview(name = "ChoiceDialog")
@Composable
fun ChoiceDialogDialogPreview() {

    DefaultDialog(
        dialogState = ChoiceDialogState(
            isShow = true,
            list = listOf("학교", "종이", "땡땡땡"),
            onPositiveClick = { title ->
                if (title is String) {
                    Log.e(DialogActivity::class.simpleName, "click ChoiceDialog clicked: $title")
                }
            },
            context = LocalContext.current
        ),
        onDismiss = {}
    )
}

@Preview(name = "DefaultDialog")
@Composable
fun DefaultDialogPreview() {
    DefaultDialog(
        dialogState = DialogState(
            message = "rkrkrkrk\nrkrkrkrk\nrkrkrkrk\nrkrkrkrk\nrkrkrkrk\nrkrkrkrk",
            positiveButtonText = "닫기",
            negativeButtonText = "확인",
            isShow = true
        ),
        onDismiss = {}
    )
}