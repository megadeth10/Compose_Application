package com.my.composeapplication.base

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.my.composeapplication.ui.theme.RED_POINT
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customScaffold(
    topAppbar : @Composable () -> Unit = {},
    bottomAppBar : @Composable () -> Unit = {},
    body : @Composable (PaddingValues) -> Unit
) : SnackbarHostState {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = topAppbar,
        content = body,
        bottomBar = bottomAppBar
    )
    return snackbarHostState
}

/**
 * 앱 종료 Composeable
 */
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun CloseToastHoisting(snackbarHost : SnackbarHostState?) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    var closeToast by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val onBackPress : () -> Unit = {
        keyboardController?.hide()
        snackbarHost?.currentSnackbarData?.dismiss()
        if (closeToast) {
            if (context is ComponentActivity) {
                context.finish()
            }
        } else {
            coroutineScope.launch {
                closeToast = true
                snackbarHost?.showSnackbar(
                    message = "한번 더 누르면 종료합니다.",
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                ).let {
                    if (it == SnackbarResult.Dismissed) {
                        closeToast = false
                    }
                }
            }
        }
    }
    BackPressHandler(onBackPressed = {
        Log.e("BMIActivity", "onBackPressed()")
        onBackPress()
    })
}

/**
 * back button callback 등록
 */
@Composable
fun BackPressHandler(
    backPressedDispatcher : OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed : () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }
    DisposableEffect(
        key1 = backPressedDispatcher,
    ) {
        backPressedDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

/**
 * 해당 Compose 영역에 vibe, color effect animation
 */
@Composable
fun highlightView(body : @Composable () -> Unit) : (Boolean) -> Unit {
    var highLight by rememberSaveable {
        mutableStateOf(false)
    }
    val setHighLight : (Boolean) -> Unit = {
        highLight = it
    }
    val color by animateColorAsState(
        targetValue = if (highLight) RED_POINT else Color.White,
        animationSpec = tween(
            durationMillis = 100,
            delayMillis = 0,
            easing = LinearEasing
        ),
        finishedListener = {
        }
    )
    val offset by animateDpAsState(
        targetValue = if (highLight) (-4).dp else 4f.dp,
        animationSpec = repeatable(
            iterations = 4,
            animation = tween(
                durationMillis = 100,
                delayMillis = 0,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        finishedListener = {
            setHighLight(false)
        }
    )
    Column(
        modifier = if (highLight) {
            Modifier
                .offset(x = offset)
        } else {
            Modifier
                .background(Color.White)
                .offset(x = 0.dp)
        }
    ) {
        body()
    }
    return setHighLight
}

/**
 * Static field, contains all scroll values
 */

data class ScrollStateParam(
    val params : String = "",
    val index : Int,
    val scrollOffset : Int
)

/**
 * Save scroll state on all time.
 * @param key value for comparing screen
 * @param params arguments for find different between equals screen
 * @param initialFirstVisibleItemIndex see [LazyListState.firstVisibleItemIndex]
 * @param initialFirstVisibleItemScrollOffset see [LazyListState.firstVisibleItemScrollOffset]
 */
@Composable
fun rememberForeverLazyListState(
    key: String = "",
    initialFirstVisibleItemIndex : Int = 0,
    initialFirstVisibleItemScrollOffset : Int = 0,
    scrollParam : ScrollStateParam? = null,
    onDispose : ((key:String, index : Int, offset : Int) -> Unit)? = null
) : LazyListState {
    val scrollState = rememberSaveable(saver = LazyListState.Saver) {
        var savedIndex = initialFirstVisibleItemIndex
        var savedOffset = initialFirstVisibleItemScrollOffset
        scrollParam?.let {
            savedIndex = it.index
            savedOffset = it.scrollOffset
        }
        LazyListState(
            savedIndex,
            savedOffset
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            val lastIndex = scrollState.firstVisibleItemIndex
            val lastOffset = scrollState.firstVisibleItemScrollOffset
            onDispose?.invoke(key, lastIndex, lastOffset)
        }
    }
    return scrollState
}
