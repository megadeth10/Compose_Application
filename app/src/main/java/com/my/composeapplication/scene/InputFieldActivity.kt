package com.my.composeapplication.scene

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.CustomScaffold
import com.my.composeapplication.base.showSnackbar
import com.my.composeapplication.ui.theme.ComposeApplicationTheme
import com.my.composeapplication.viewmodel.InputFieldViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by YourName on 2022/08/16.
 * InputField 샘픔 코드
 */

// TODO back key에 대한 focus clear를 구현해야함.

@AndroidEntryPoint
class InputFieldActivity : BaseComponentActivity() {
    private val viewModel : InputFieldViewModel by viewModels()
    override fun getContent() : @Composable () -> Unit = {
        Log.e(this::class.simpleName, "getContent()")
        ComposeApplicationTheme {
            InputFieldHoisting()
        }
    }
}

@Composable
private fun InputFieldHoisting() {
    val viewModel = viewModel<InputFieldViewModel>()
    val textValue = viewModel.inputValue
    CustomScaffold(snackbarHostState = viewModel.snackbarState) {
        InputField(initValue = textValue.value) {
            viewModel.changeInput(it)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InputField(modifier : Modifier = Modifier, initValue : String, valueChange : (String) -> Unit) {
    val viewModel = viewModel<InputFieldViewModel>()
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
            .clearAndSetSemantics { }
            .background(Color.Transparent),
//            .focusRequester(focusRequester),
            value = initValue,
            onValueChange = {
                valueChange(it)
                viewModel.changeInput(it)
            },
//            label = { InputFieldLabel(textColor = Color.White) },
            placeholder = { InputFieldLabel("무엇이든 입력해 주세요.") }
        )
        Button(onClick = {
            focusManager.clearFocus()
            keyboardController?.hide()
            viewModel.showSnackbar(
                message = "Snackbar # ${initValue}",
                actionLabel = "닫기"
            )
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