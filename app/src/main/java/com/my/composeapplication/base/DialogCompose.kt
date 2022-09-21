package com.my.composeapplication.base

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
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
    onDismiss : () -> Unit,
    snackbarHostState : SnackbarHostState
) {
    dialogState.list?.filterIsInstance<String>()?.let { list ->
        var selected by rememberSaveable {
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
                        .clickable { onClick(title) }
                        .padding(vertical = 5.dp),
                    verticalAlignment = CenterVertically
                ) {
                    RadioButton(
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
            snackbarHostState = snackbarHostState,
            negativeButtonText = dialogState.negativeButtonText,
            onNegativeClick = { snackbarState ->
                dialogState.onNegativeClick?.invoke(snackbarState)
            },
            positiveButtonText = dialogState.positiveButtonText,
            onPositiveClick = { snackbarState, data ->
                dialogState.onPositiveClick?.invoke(snackbarState, selected)
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
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    if (dialogState.isShow) {
        Dialog(
            onDismissRequest = { onDismiss() },
        ) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Box(
                modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                SnackbarWrappingCompose(
                    snackbarHostState = snackbarHostState,
                    onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        when (dialogState.useChildComposable) {
                            DialogType.ChoiceDialog -> {
                                ChoiceDialog(
                                    modifier = Modifier.fillMaxWidth(),
                                    dialogState = dialogState as ChoiceDialogState,
                                    onDismiss = onDismiss,
                                    snackbarHostState = snackbarHostState
                                )
                            }
                            else -> {
                                SimpleDialogLayout(
                                    dialogState = dialogState,
                                    onDismiss = onDismiss,
                                    snackbarHostState = snackbarHostState
                                )
                            }
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
    snackbarHostState : SnackbarHostState,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 20.dp),
        text = dialogState.message,
        color = MaterialTheme.colorScheme.secondary
    )
    DialogButtonRow(
        snackbarHostState = snackbarHostState,
        negativeButtonText = dialogState.negativeButtonText,
        onNegativeClick = dialogState.onNegativeClick,
        positiveButtonText = dialogState.positiveButtonText,
        onPositiveClick = dialogState.onPositiveClick,
        onDismiss = onDismiss
    )
}

@Composable
fun DialogButtonRow(
    snackbarHostState : SnackbarHostState,
    negativeButtonText : String? = null,
    onNegativeClick : ((SnackbarHostState) -> Unit)? = null,
    positiveButtonText : String = "",
    onPositiveClick : ((SnackbarHostState, data : Any?) -> Unit)? = null,
    onDismiss : () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        negativeButtonText?.let {
            DialogButton(
                modifier = Modifier
                    .background(Color.Gray)
                    .weight(1f),
                buttonText = it,
                onClick = {
                    onNegativeClick?.invoke(snackbarHostState) ?: onDismiss()
                }
            )
        }
        DialogButton(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .weight(1f),
            buttonText = positiveButtonText,
            onClick = {
                onPositiveClick?.invoke(snackbarHostState, null) ?: onDismiss()
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
        modifier = modifier.height(40.dp),
        onClick = { onClick() },
        shape = RectangleShape
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
            onPositiveClick = { snackbarState, data ->
                if (data is String) {
                    Log.e(DialogActivity::class.simpleName, "click ChoiceDialog clicked: $data")
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