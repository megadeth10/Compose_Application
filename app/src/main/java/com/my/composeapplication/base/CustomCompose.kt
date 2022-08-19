package com.my.composeapplication.base

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.my.composeapplication.ui.theme.RED_POINT
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customScaffold(
    modifier: Modifier = Modifier,
    topAppbar : @Composable () -> Unit = {},
    bottomAppBar : @Composable () -> Unit = {},
    body : @Composable (PaddingValues) -> Unit
) : SnackbarHostState {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = modifier,
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
    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }
    // If `backDispatcher` changes, dispose and reset the effect
    DisposableEffect(
        key1 = backPressedDispatcher,
    ) {
        // Add callback to the backDispatcher
        backPressedDispatcher?.addCallback(backCallback)
        // When the effect leaves the Composition, remove the callback
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

/**
 * Nested scroll 고정 영역과 삭제 영역을 구현함.
 */
@Composable
fun NestedScrollCompose(
    toolbarHeight : Dp = 48.dp,
    toolbarFixedHeight : Dp = 30.dp,
    topAppbar : @Composable (height:Dp, offsetHeightPx:Int) -> Unit,
    fixedContainer: @Composable (height:Dp, offsetHeightPx:Int)->Unit,
    body: @Composable (topPadding: Dp)-> Unit
){
    // here we use LazyColumn that has build-in nested scroll, but we want to act like a
// parent for this LazyColumn and participate in its nested scroll.
// Let's make a collapsing toolbar for LazyColumn
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarFixedHeightPx = with(LocalDensity.current) { toolbarFixedHeight.roundToPx().toFloat() }
// our offset to collapse toolbar
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
// now, let's create connection to the nested scroll system and listen to the scroll
// happening inside child LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available : Offset, source : NestedScrollSource) : Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
                return Offset.Zero
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            // attach as a parent to the nested scroll system
            .nestedScroll(nestedScrollConnection)
    ) {
        body(toolbarHeight + toolbarFixedHeight)
        topAppbar(toolbarHeight, toolbarOffsetHeightPx.value.roundToInt())
//        TopAppBar(
//            modifier = Modifier
//                .height(toolbarHeight)
//                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
//            title = { Text("toolbar offset is ${toolbarOffsetHeightPx.value}") }
//        )
        fixedContainer(toolbarFixedHeight, toolbarHeightPx.toInt() + toolbarOffsetHeightPx.value.roundToInt())
//        Text(
//            text = "Fixed Title",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(toolbarFixedHeight)
//                .offset { IntOffset(x = 0, y = toolbarHeightPx.toInt() + toolbarOffsetHeightPx.value.roundToInt()) }
//                .background(Color.Red)
//        )
    }
}
