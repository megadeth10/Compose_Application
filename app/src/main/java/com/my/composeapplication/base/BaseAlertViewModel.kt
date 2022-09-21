package com.my.composeapplication.base

import android.view.WindowManager
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.my.composeapplication.base.data.DialogState
import com.my.composeapplication.base.data.PopupState

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
     * Dialog State
     */
    private val _dialogState : MutableState<DialogState> = mutableStateOf(DialogState())
    val dialogState : State<DialogState> get() = this._dialogState

    /**
     * Popup State
     */
    @Deprecated(message = "PopUpView의 anchor 설정이 되지 않고 anchor할 Compose를 wrapping 하는 형태로 하기 때문에 공통부로 사용이 필요없을것 같다.")
    private val _popState : MutableState<PopupState> = mutableStateOf(PopupState())
    @Deprecated(message = "PopUpView의 anchor 설정이 되지 않고 anchor할 Compose를 wrapping 하는 형태로 하기 때문에 공통부로 사용이 필요없을것 같다.")
    val popState : State<PopupState> get() = this._popState

    /**
     * 키보드가 스넥바를 가리는 문제를 체크하기 위한 Activity InputMode Check
     */
    private var isAdjustInputMode: Boolean = false

    /**
     * popupShow
     */
    @Deprecated(message = "PopUpView의 anchor 설정이 되지 않고 anchor할 Compose를 wrapping 하는 형태로 하기 때문에 공통부로 사용이 필요없을것 같다.")
    fun showPopup(popupState : PopupState) {
        this._popState.value = popupState
    }

    /**
     * popupClose
     */
    @Deprecated(message = "PopUpView의 anchor 설정이 되지 않고 anchor할 Compose를 wrapping 하는 형태로 하기 때문에 공통부로 사용이 필요없을것 같다.")
    fun dismissPopup() {
        this._popState.value = PopupState(isShow = false)
    }

    /**
     * Activity Input mode
     */
    fun setInputMode(mode:Int) {
        isAdjustInputMode = mode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
    }
    fun isAdjustInputMode() = this.isAdjustInputMode

    /**
     * Dialog 닫기
     */
    fun dismissDialog() {
        this._dialogState.value = DialogState(isShow = false)
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
     * Snackbar 출력 wrapping function
     */
    fun showSnackbar(
        message : String,
        actionLabel : String? = null,
        withDismissAction : Boolean = false,
        duration : SnackbarDuration = SnackbarDuration.Short,
        onResult : ((SnackbarResult) -> Unit)? = null
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