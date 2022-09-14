package com.my.composeapplication.base

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.my.composeapplication.ui.theme.RED_POINT
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    modifier : Modifier = Modifier,
    snackbarHostState : State<SnackbarHostState> = remember {
        mutableStateOf(SnackbarHostState())
    },
    topAppbar : @Composable () -> Unit = {},
    bottomAppBar : @Composable () -> Unit = {},
    body : @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState.value) },
        topBar = topAppbar,
        content = body,
        bottomBar = bottomAppBar
    )
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

// TODO Infinity List Compose 추가함.
@Composable
fun LazyListState.OnEndItem(
    threshold : Int = 0,
    loadMore : () -> Unit
) {
    val isEndItem = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf true
            lastVisibleItem.index >= layoutInfo.totalItemsCount - (threshold + 1)
        }
    }
    LaunchedEffect(key1 = isEndItem) {
        snapshotFlow { isEndItem.value }
            .collect {
                // if should load more, then invoke loadMore
                if (it) loadMore()
            }
    }
}

/**
 * Nested scroll 고정 영역과 삭제 영역을 구현함.
 */
@Composable
fun NestedScrollCompose(
    toolbarHeight : Dp = 48.dp,
    topAppbar : @Composable (height : Dp, offsetHeightPx : Int) -> Unit,
    fixedContainer : @Composable() ((height : Dp, offsetHeightPx : Int) -> Unit)? = null,
    toolbarFixedHeight : Dp = if (fixedContainer == null) 0.dp else 30.dp,
    foldingAnimation : Boolean = false,
    body : @Composable (topPadding : Dp, (Float) -> Unit) -> Unit
) {
    // here we use LazyColumn that has build-in nested scroll, but we want to act like a
// parent for this LazyColumn and participate in its nested scroll.
// Let's make a collapsing toolbar for LazyColumn
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
// our offset to collapse toolbar
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val setToolbarOffset : (Float) -> Unit = {
        toolbarOffsetHeightPx.value = it
    }
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
            .nestedScroll(nestedScrollConnection),
    ) {
        val animationHeight = when (foldingAnimation) {
            true -> {
                val changeValue = abs(toolbarOffsetHeightPx.value.roundToInt())
                val delta = toolbarHeightPx / changeValue
                toolbarFixedHeight / delta
            }
            else -> {
                toolbarFixedHeight
            }
        }
        body(toolbarHeight + animationHeight, setToolbarOffset)
        fixedContainer?.invoke(animationHeight, toolbarHeightPx.toInt() + toolbarOffsetHeightPx.value.roundToInt())
        topAppbar(toolbarHeight, toolbarOffsetHeightPx.value.roundToInt())
    }
}

/**
 * Scroll Top Button Compose
 * @param setOffset NetstedScroll을 사용할경우 title toolbar scroll 설정을 위함.
 * TODO: content의 LazyColumn의 아이팀이 하나일때만 가능하다 scroll의 offset전체를 알방법이 필요하다.
 */
@Composable
fun ScrollTopButtonCompose(
    scrollState : LazyListState,
    setOffset : (Float) -> Unit,
    content : @Composable () -> Unit
) {
    val density = LocalDensity.current
    val screenHeightPx = with(density) {
        LocalConfiguration.current.screenHeightDp.dp.roundToPx() * 0.8
    }
    val showButton by remember {
        derivedStateOf {
            val firstOffset = scrollState.firstVisibleItemScrollOffset
            firstOffset > screenHeightPx
        }
    }

    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        content()
        AnimatedVisibility(visible = showButton,
            enter = slideInVertically {
                with(density) {
                    40.dp.roundToPx()
                }
            },
            exit = slideOutVertically {
                with(density) {
                    40.dp.roundToPx()
                }
            }
        ) {
            IconButton(
                modifier = Modifier
                    .offset(y = (-10).dp)
                    .border(1.dp, Color.Black, CircleShape)
                    .size(30.dp),
                onClick = {
                    coroutineScope.launch {
                        setOffset(0f)
                        scrollState.animateScrollToItem(0)
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = null)
            }
        }
    }
}

// TODO viewToMeasure로 그려질 compose의 width를 계산
@Composable
fun MeasureUnconstrainedViewWidth(
    viewToMeasure : @Composable () -> Unit,
    content : @Composable (measuredWidth : Dp) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measuredWidth = subcompose("viewToMeasure", viewToMeasure)[0]
            .measure(Constraints()).width.toDp()

        val contentPlaceable = subcompose("content") {
            content(measuredWidth)
        }[0].measure(constraints)
        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.place(0, 0)
        }
    }
}

/**
 * refresh Swipe Compose
 */
@Composable
fun SwipeRefreshCompose(
    isRefresh : Boolean,
    onRefresh : () -> Unit,
    content : @Composable () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefresh),
        onRefresh = onRefresh,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                // Pass the SwipeRefreshState + trigger through
                state = state,
                refreshTriggerDistance = trigger,
                // Enable the scale animation
                scale = true,
                // Change the color and shape
                backgroundColor = Color.White,
                shape = CircleShape,
            )
        }
    ) {
        content()
    }
}

