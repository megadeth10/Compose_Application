package com.my.composeapplication.scene

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.DefaultSnackbar
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
        dialogSetFunction = makeDialog()
        DialogMainScreen()

    }

    override fun onDestroy() {
        super.onDestroy()
        dialogSetFunction = null
    }
}

private var dialogSetFunction : ((Boolean) -> Unit)? = null
private var popupSetFunction : ((Boolean) -> Unit)? = null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogMainScreen() {
    val viewModel = viewModel<DialogViewModel>()
    val scope = rememberCoroutineScope()
    Column {
        CustomTopAppBar(title = "DialogActivity")
        DefaultSnackbar(viewModel.snackbarState.value, onDismiss = viewModel::dismissSnackbar) {
            Column {
                Button(
                    onClick = {
                        dialogSetFunction?.invoke(true)
                    }
                ) {
                    Text(text = "Click show Dialog")
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
    }
}

@Composable
fun makeDialog() : (Boolean) -> Unit {
    val (openDialog, showDialog) = remember { mutableStateOf(false) }
    val dialogWidth = 200.dp
    val dialogHeight = 50.dp

    if (openDialog) {
        Dialog(onDismissRequest = { showDialog(false) }) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Box(
                Modifier
                    .size(dialogWidth, dialogHeight)
                    .background(Color.White)
            ) {
                Text(text = "Dialog")
                Button(onClick = { showDialog(false) }) {
                    Text("Close")
                }
            }
        }
    }
    return showDialog
}