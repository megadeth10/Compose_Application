package com.my.composeapplication.scene

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.DefaultDialog
import com.my.composeapplication.base.DefaultSnackbar
import com.my.composeapplication.base.data.ChoiceDialogState
import com.my.composeapplication.base.data.DialogState
import com.my.composeapplication.base.data.DialogType
import com.my.composeapplication.scene.bmi.CustomTopAppBar
import com.my.composeapplication.viewmodel.DialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by YourName on 2022/08/26.
 */
@AndroidEntryPoint
class DialogActivity : BaseComponentActivity() {
    private val viewModel by viewModels<DialogViewModel>()
    override fun getContent() : @Composable () -> Unit = {
        DialogMainScreen()
    }
}

private var popupSetFunction : ((Boolean) -> Unit)? = null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogMainScreen() {
    val context = LocalContext.current
    val viewModel = viewModel<DialogViewModel>()
    val scope = rememberCoroutineScope()
    Column {
        CustomTopAppBar(title = "DialogActivity")
        DefaultSnackbar(viewModel.snackbarState.value, onDismiss = viewModel::dismissSnackbar) {
            Column {
                Button(
                    onClick = {
                        viewModel.showDialog(
                            DialogState(
                                message = "안녕하세요",
                                negativeButtonText = "닫기",
                                positiveButtonText = "저장",
                                onPositiveClick = {
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
                                list = listOf("학교","종이","땡땡땡"),
                                onPositiveClick = { title ->
                                    if (title is String && title.isNotEmpty()) {
                                        Log.e(DialogActivity::class.simpleName, "click ChoiceDialog clicked: $title")
                                    } else {
                                        scope.launch {
                                            viewModel.showSnackbar("선택하시오")
                                        }

                                    }
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
                        scope.launch {
                            viewModel.showSnackbar(
                                message = "aaaaa",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                ) {
                    Text(text = "Click show snackbar")
                }
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.showSnackbar(
                                message = "aaaaa",
                                duration = SnackbarDuration.Long,
                                actionLabel = "가자",
                            ).let {
                                when (it) {
                                    SnackbarResult.Dismissed -> Log.d("SnackbarDemo", "Dismissed")
                                    SnackbarResult.ActionPerformed -> Log.d("SnackbarDemo", "Snackbar's button clicked")
                                }
                            }
                        }
                    }
                ) {
                    Text(text = "Click show snackbar")
                }
            }
        }
        DefaultDialog(
            dialogState = viewModel.dialogState.value,
            onDismiss = viewModel::dismissDialog
        )
    }
}