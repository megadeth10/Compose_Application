package com.my.composeapplication.base

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.my.composeapplication.base.data.DialogState
import kotlinx.coroutines.CoroutineScope

/**
 * Created by YourName on 2022/08/29.
 */
/**
 * Notice
 * SnackbarResult wrong result
 * ref : https://github.com/android/compose-samples/issues/781
 * action을 클릭해도 상태가 Dismissed로 떨어 진다. 문제점이고
 */
abstract class BaseAlertViewModel : ViewModel() {
    /**
     * SnackBar State
     */
    private val _snackbarState : MutableState<SnackbarHostState> = mutableStateOf(SnackbarHostState())
    val snackbarState : State<SnackbarHostState> get() = this._snackbarState

    private val _dialogState : MutableState<DialogState> = mutableStateOf(DialogState())
    val dialogState : State<DialogState> get() = this._dialogState

    /**
     * Dialog 닫기
     */
    fun dismissDialog() {
        val currentValue = DialogState()
        currentValue.isShow = false
        this._dialogState.value = currentValue
    }

    /**
     * Dialog 열기
     */
    fun showDialog(
        dialogState : DialogState
    ) {
        dialogState.isShow = true
        this._dialogState.value = dialogState
    }

    /**
     * Snackbar 닫기
     */
    fun dismissSnackbar() {
        this.snackbarState.value.currentSnackbarData?.dismiss()
    }

    /**
     * Snackbar 출력
     */
    fun showSnackbar(
        message : String,
        actionLabel : String? = null,
        withDismissAction : Boolean = false,
        duration : SnackbarDuration = SnackbarDuration.Short,
        onResult: ((SnackbarResult) -> Unit)? = null
    ) {
        showSnackbar(
            snackbarHostState = snackbarState.value,
            message = message,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration,
            onResult = onResult
        )
    }
}