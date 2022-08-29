package com.my.composeapplication.base.data

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

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

    /**
     * Snackbar 닫기
     */
    fun dismissSnackbar() {
        this.snackbarState.value.currentSnackbarData?.dismiss()
    }

    /**
     * Snackbar 출력
     */
    suspend fun showSnackbar(
        message : String,
        actionLabel : String? = null,
        withDismissAction : Boolean = false,
        duration : SnackbarDuration = SnackbarDuration.Short
    ): SnackbarResult {
        dismissSnackbar()
        return this@BaseAlertViewModel.snackbarState.value.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration
        )
    }
}