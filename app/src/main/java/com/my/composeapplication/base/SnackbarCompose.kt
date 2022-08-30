package com.my.composeapplication.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by YourName on 2022/08/30.
 */
/**
 * Snack Composable
 * 문제점
 * 1. adjustResize가 아닐때, 키보드 노출에 의한 표시 정렬 위치를 해결해야함.
 */
@Composable
fun DefaultSnackbar(
    snackbarHostState : SnackbarHostState,
    modifier : Modifier = Modifier,
    isShowTop : Boolean = false,
    onDismiss : () -> Unit = { },
    content : @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        content()
        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { data ->
                Snackbar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    action = {
                        data.visuals.actionLabel?.let { actionLabel ->
                            TextButton(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd),
                                onClick = onDismiss
                            ) {
                                Text(
                                    text = actionLabel,
                                )
                            }
                        }
                    }
                ) {
                    // Snackbar의 action과 text 영역이 곁치는 문제로 대략의 비율로 width 정의함.
                    Text(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        text = data.visuals.message,
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(
                    if (isShowTop) {
                        Alignment.TopCenter
                    } else {
                        Alignment.BottomCenter
                    }
                )
        )
    }

    DisposableEffect(key1 = snackbarHostState) {
        onDispose {
            onDismiss()
        }
    }
}

/**
 * 기존 snackbar를 해제 하고 출력해야 하기때문에 이 함수로 대체한다.
 */
fun showSnackbar(
    snackbarHostState : SnackbarHostState?,
    message : String,
    actionLabel : String? = null,
    withDismissAction : Boolean = false,
    duration : SnackbarDuration = SnackbarDuration.Short,
    onResult : ((SnackbarResult) -> Unit)? = null
) {
    snackbarHostState?.let {
        it.currentSnackbarData?.dismiss()
        CoroutineScope(Dispatchers.Main).launch {
            val result = it.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration
            )
            onResult?.invoke(result)
        }
    }
}