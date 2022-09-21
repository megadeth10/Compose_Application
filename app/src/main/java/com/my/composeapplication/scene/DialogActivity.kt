package com.my.composeapplication.scene

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.my.composeapplication.R
import com.my.composeapplication.base.*
import com.my.composeapplication.base.data.ChoiceDialogState
import com.my.composeapplication.base.data.DialogState
import com.my.composeapplication.base.data.DropDownMenuState
import com.my.composeapplication.base.data.PopupState
import com.my.composeapplication.scene.bmi.CustomTopAppBar
import com.my.composeapplication.viewmodel.DialogViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by YourName on 2022/08/26.
 * Dialog, Snackbar, PopupView, DropDown Menu 샘플 코드
 */
@AndroidEntryPoint
class DialogActivity : BaseComponentActivity() {
    private val viewModel by viewModels<DialogViewModel>()
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setInputMode(window.attributes.softInputMode)
    }

    override fun getContent() : @Composable () -> Unit = {
        DialogMainScreen()
        DropDownView()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogMainScreen() {
    val context = LocalContext.current
    val viewModel = viewModel<DialogViewModel>()
    val closeText = stringResource(id = R.string.close)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CustomTopAppBar(title = "DialogActivity")
        SnackbarWrappingCompose(
            snackbarHostState = viewModel.snackbarState.value,
            onDismiss = viewModel::dismissSnackbar,
            modifier = Modifier.fillMaxSize(),
            isAdjustResizeMode = viewModel.isAdjustInputMode()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                DropDownMenuButton()
                Button(
                    onClick = {
                        viewModel.showDialog(
                            DialogState(
                                message = "안녕하세요",
                                negativeButtonText = "닫기",
                                positiveButtonText = "저장",
                                onPositiveClick = { snackbarState, data ->
                                    Log.e(DialogActivity::class.simpleName, "저장 눌렀지?")
                                }
                            )
                        )
                    }
                ) {
                    Text(text = "Click show Dialog")
                }
                Button(
                    onClick = {
                        viewModel.showDialog(
                            ChoiceDialogState(
                                context = context,
                                isShow = true,
                                list = listOf("학교", "종이", "땡땡땡"),
                                onPositiveClick = { snackbarState, data ->
                                    if (data is String && data.isNotEmpty()) {
                                        Log.e(DialogActivity::class.simpleName, "click ChoiceDialog clicked: $data")
                                        viewModel.dismissDialog()
                                    } else {
                                        showSnackbar(
                                            snackbarHostState = snackbarState,
                                            message = "선택하시오",
                                            duration = SnackbarDuration.Short,
                                            actionLabel = closeText
                                        )
                                    }
                                },
                                onNegativeClick = {
                                    viewModel.dismissDialog()
                                }
                            )
                        )
                    }
                ) {
                    Text(text = "Click show Choice Dialog")
                }
                TextField(value = "", onValueChange = {})
                Button(
                    onClick = {
                        viewModel.showSnackbar(
                            message = "aaaaa",
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                ) {
                    Text(text = "Click show snackbar")
                }
                Button(
                    onClick = {
                        viewModel.showSnackbar(
                            message = "aaaaa",
                            duration = SnackbarDuration.Short,
                            actionLabel = "가자",
                            onResult = {
                                when (it) {
                                    SnackbarResult.Dismissed -> Log.d("SnackbarDemo", "Dismissed")
                                    SnackbarResult.ActionPerformed -> Log.d("SnackbarDemo", "Snackbar's button clicked")
                                }
                            }
                        )
                    }
                ) {
                    Text(text = "Click show snackbar")
                }
                PopupViewButton(
                    buttonText = "Click show Popup",
                    popupText = "adfadfasdfasdf"
                )
                PopupViewButton(
                    buttonText = "Click show Popup22222",
                    popupText = "adfadfasdfasdf\nasdfasdf"
                )
                Button(
                    onClick = {
                        viewModel.dismissPopup()
                    }
                ) {
                    Text(text = "Click close Popup")
                }
                PopupViewUserContainer(modifier = Modifier.fillMaxWidth())

            }
        }
        DefaultDialog(
            dialogState = viewModel.dialogState.value,
            onDismiss = viewModel::dismissDialog
        )
    }
}

@Composable
fun DropDownMenuButton() {
    val initText = stringResource(id = R.string.please_choice)
    val optionList = LocalContext.current.resources.getStringArray(R.array.option)
    var selectedText by rememberSaveable {
        mutableStateOf(initText)
    }
    DropDownMenuCompose(
        dropDownMenuState = DropDownMenuState(
            menuList = optionList.toList()
        ),
        modifier = Modifier
            .width(200.dp)
            .height(50.dp)
            .border(1.dp, Color.Black),
        onMenuClick = {
            selectedText = it
        },
        selectedItem = selectedText
    ) { onClick, isShow ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick.invoke(null)
                }
                .padding(3.dp),
            verticalAlignment = CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = selectedText
            )
            Icon(
                imageVector = if (isShow)
                    Icons.Default.ArrowDropUp
                else
                    Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }
}

/**
 * 중요 포인터 PopupView의 Anchor가 없는 이유는 Box로 감싸기만 하면 알아서 표시 된다.
 */
@Composable
fun PopupViewButton(modifier : Modifier = Modifier, buttonText : String, popupText : String) {
    PopupViewCompose(
        modifier = modifier,
        popupState = PopupState(content = popupText)
    ) {
        Button(
            onClick = {
                it.invoke(null)
            }
        ) {
            Text(text = buttonText)
        }
    }
}

@Composable
fun PopupViewUserContainer(modifier : Modifier = Modifier) {
    var counter by remember {
        mutableStateOf(1)
    }
    val dp1 = with(LocalDensity.current) {
        1.dp.roundToPx()
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Cyan)
    ) {
        Text(
            text = "aaaaa",
            modifier = Modifier
                .weight(1f)
                .background(Color.Magenta)
                .align(CenterVertically),
            color = Color.White
        )
        PopupViewCompose(
            popupState = PopupState(content = "It's own me $counter", anchor = IntOffset(0, dp1 * 20))
        ) {

            Button(onClick = {
                counter += 1
                it.invoke(PopupState(content = "It's own me $counter", anchor = IntOffset(0, dp1 * 20)))
                Log.e("LEE", "counter: $counter")
            }) {
                Text("click")
            }
        }
    }

}

@Composable
fun DropDownView() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("A", "B", "C", "D", "E", "F")
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            items[selectedIndex], modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(
                    Color.Gray
                )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expanded = false
                    },
                    text = {
                        val disabledText = if (s == disabledValue) {
                            " (Disabled)"
                        } else {
                            ""
                        }
                        Text(text = s + disabledText)
                    }
                )
            }
        }
    }
}

@Composable
fun PopupViewHoisting(modifier : Modifier = Modifier, content : @Composable () -> Unit) {
    val viewModel : DialogViewModel = viewModel(LocalContext.current as BaseComponentActivity)
    PopupView(
        modifier = modifier,
        popupState = viewModel.popState.value,
        onClose = viewModel::dismissPopup
    )
}

@Preview(name = "PopupView")
@Composable
fun PopupViewPreview() {
    PopupContentView(
        content = "popupState.content",
    )
}