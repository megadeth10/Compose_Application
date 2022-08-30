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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.my.composeapplication.ui.theme.RED_POINT
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customScaffold(
    modifier : Modifier = Modifier,
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
 * Nested scroll 고정 영역과 삭제 영역을 구현함.
 */
@Composable
fun NestedScrollCompose(
    toolbarHeight : Dp = 48.dp,
    toolbarFixedHeight : Dp = 30.dp,
    topAppbar : @Composable (height : Dp, offsetHeightPx : Int) -> Unit,
    fixedContainer : @Composable ((height : Dp, offsetHeightPx : Int) -> Unit)? = null,
    body : @Composable (topPadding : Dp) -> Unit
) {
    // here we use LazyColumn that has build-in nested scroll, but we want to act like a
// parent for this LazyColumn and participate in its nested scroll.
// Let's make a collapsing toolbar for LazyColumn
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
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
        fixedContainer?.invoke(toolbarFixedHeight, toolbarHeightPx.toInt() + toolbarOffsetHeightPx.value.roundToInt())
    }
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
            lastVisibleItem.index - threshold == layoutInfo.totalItemsCount - (threshold + 1)
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun <T> InfinityHorizontalPager(
    modifier : Modifier = Modifier,
    list : List<T>,
    pagerState : PagerState,
    content : @Composable (Modifier, T, Int) -> Unit
) {
    if (list.isEmpty()) {
        return
    }
    // Display 10 items
    val pageCount = list.size
    val maxSize = Int.MAX_VALUE
    val startIndex = maxSize / 2
    HorizontalPager(
        // Set the raw page count to a really large number
        count = maxSize,
        state = pagerState,
        modifier = modifier.fillMaxWidth()
    ) { index ->
        // We calculate the page from the given index
        var page = (index - startIndex).floorMod(pageCount)
        page = if (list.size > page) page else 0
        val item = list[page]
        content(Modifier.fillMaxSize(), item, page)
    }
}

/**
 * Infinity Pager 사용시에 사용
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberInfinityPagerState(initialPage : Int = 0) : PagerState {
    // We start the pager in the middle of the raw number of pages
    return rememberPagerState(initialPage = (Int.MAX_VALUE / 2) + initialPage)
}

fun Int.floorMod(other : Int) : Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}

/**
 * Infinity Pager Indicator
 */
@Composable
fun DotsIndicator(
    modifier : Modifier = Modifier,
    totalDots : Int,
    selectedIndex : Int,
    selectedColor : Color = Color.White,
    unSelectedColor : Color = Color.Gray,
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(7.dp))
            .background(Color.Gray.copy(alpha = .3f))
            .padding(
                vertical = 3.dp,
                horizontal = 5.dp
            )
    ) {
        items(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == selectedIndex) {
                            selectedColor
                        } else {
                            unSelectedColor
                        }
                    )
            )
            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}