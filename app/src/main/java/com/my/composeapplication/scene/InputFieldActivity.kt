package com.my.composeapplication.scene

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.customScaffold
import com.my.composeapplication.base.showSnackbar
import com.my.composeapplication.ui.theme.ComposeApplicationTheme

/**
 * Created by YourName on 2022/08/16.
 */

// TODO back key에 대한 focus clear를 구현해야함.
private var snackbarHostState : SnackbarHostState? = null

class InputFieldActivity : BaseComponentActivity() {
    override fun getContent() : @Composable () -> Unit = {
        Log.e(this::class.simpleName, "getContent()")
        ComposeApplicationTheme {
            InputFieldHoisting()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbarHostState = null
    }
}

@Composable
private fun InputFieldHoisting(initValue : String = "") {
    var textValue by rememberSaveable {
        mutableStateOf(initValue)
    }
    snackbarHostState = customScaffold {
        InputField(initValue = textValue) {
            textValue = it
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InputField(modifier : Modifier = Modifier, initValue : String, valueChange : (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
//    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .clearAndSetSemantics {  }
            .background(Color.Transparent),
//            .focusRequester(focusRequester),
            value = initValue,
            onValueChange = valueChange,
//            label = { InputFieldLabel(textColor = Color.White) },
            placeholder = { InputFieldLabel("무엇이든 입력해 주세요.") }
        )
        Button(onClick = {
            snackbarHostState?.let {
                focusManager.clearFocus()
                keyboardController?.hide()
                showSnackbar(
                    snackbarHostState = it,
                    message = "Snackbar # ${initValue}",
                    actionLabel = "닫기"
                )
            }
        }) {
            Text("apply")
        }
    }
}

@Composable
fun InputFieldLabel(label : String = "title", textColor : Color = Color.Gray) {
    Text(
        text = label,
        color = textColor
    )
}

@Preview(widthDp = 320, name = "Preview")
@Composable
fun Preview() {
    InputFieldHoisting()
}