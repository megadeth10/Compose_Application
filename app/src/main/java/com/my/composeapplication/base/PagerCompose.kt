package com.my.composeapplication.base

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import kotlinx.coroutines.*

/**
 * Created by YourName on 2022/09/14.
 * View Pager Compose
 */

private const val MaxIndex = Int.MAX_VALUE

/**
 * pager Timer 시작 함수
 */
@OptIn(ExperimentalPagerApi::class)
fun startPagerTimer(
    coroutineScope : CoroutineScope,
    size : Int,
    autoScroll : Boolean,
    pagerState : PagerState,
    expiredCallback : (() -> Unit)? = null
) : Job? {
    if (autoScroll && size > 1) {
        return coroutineScope.launch(Dispatchers.Default) {
//            Log.e("PagerCompose", "startPagerTimer() start")
            try {
                expiredTimer {
                    coroutineScope.launch(Dispatchers.Main) {
                        expiredCallback?.invoke() ?: pagerState.animateScrollToPage(nextPager(pagerState, size), 0f)
                    }
                }
            } catch (ex : CancellationException) {
//                Log.e("PagerCompose", "startPagerTimer() cancel")
            }
        }
    }
    return null
}

/**
 * 시간 말료 함수
 */
private suspend fun expiredTimer(
    delayMillisecond : Long = 5000,
    expiredCallback : () -> Unit
) {
    delay(delayMillisecond)
    expiredCallback()
}

/**
 * pager set next index
 */
@OptIn(ExperimentalPagerApi::class)
private fun nextPager(
    state : PagerState,
    listSize : Int,
    isInfinity : Boolean = false
) : Int {
    val nextIndex = state.currentPage + 1
    val maxIndex = if (isInfinity) {
        MaxIndex
    } else {
        listSize
    }
    return if (isInfinity) {
        if (maxIndex == nextIndex) {
            maxIndex / 2
        } else {
            nextIndex
        }
    } else {
        if (maxIndex == nextIndex) {
            0
        } else {
            nextIndex
        }
    }
}

/**
 * ended roll pager
 * @param pagerContent : interactionSource는 하위 Compose에
 * Modifier.clickable(interactionSource = interactionSource,
 * indication = null) 로 연결해 줘서 클릭 down/up시에 타이머를 체크 할수 있도록 한다.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun <T> HorizontalPagerCompose(
    modifier : Modifier = Modifier,
    initialPage : Int = 0,
    list : List<T>,
    autoScroll : Boolean = false,
    isInfinity : Boolean = false,
    indicatorContent : @Composable BoxScope.(
        pageState : PagerState,
        item : T,
        pageIndex : Int
    ) -> Unit,
    pagerContent : @Composable PagerScope.(
        interactionSource : MutableInteractionSource,
        item : T,
        pageIndex : Int
    ) -> Unit,
) {
    val realPageCount = list.size
    var pagerState = rememberPagerState(initialPage)
    var maxSize = realPageCount
    var startIndex = 0
    var job : Job? by remember {
        mutableStateOf(null)
    }
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    if (isInfinity) {
        pagerState = rememberInfinityPagerState(initialPage)
        maxSize = MaxIndex
        startIndex = maxSize / 2
    }

    LaunchedEffect(key1 = pagerState.currentPage, key2 = list.size) {

        job?.cancel()
        job = startPagerTimer(
            coroutineScope = coroutineScope,
            size = realPageCount,
            autoScroll = autoScroll,
            pagerState = pagerState,
        )
    }
    LaunchedEffect(key1 = interactionSource, key2 = list.size) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    job?.cancel()
//                    Log.e("LEE", "HeaderPagerCompose() PressInteraction.Press")
                }
                is PressInteraction.Release,
                is PressInteraction.Cancel -> {
//                    Log.e("LEE", "HeaderPagerCompose() PressInteraction.Cancel")
                    job = startPagerTimer(
                        coroutineScope = coroutineScope,
                        size = realPageCount,
                        autoScroll = autoScroll,
                        pagerState = pagerState,
                    )
                }
            }
        }
    }

    Box(
        modifier
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .placeholder(
                    visible = list.isEmpty(),
                    color = Color.Gray,
                    shape = RectangleShape,
                    highlight = PlaceholderHighlight.shimmer(Color.White)
                ),
            count = maxSize,
            state = pagerState,
        ) { pagerIndex ->
            if (realPageCount > 1) {
                var realIndex = getRealIndex(
                    pagerIndex = pagerIndex,
                    listSize = realPageCount,
                    startIndex = startIndex,
                    isInfinity = isInfinity
                )
                realIndex = if (list.size > realIndex) realIndex else 0
                val item = list[realIndex]
                pagerContent(interactionSource, item, realIndex)
            }
        }
        if (realPageCount > 1) {
            val realIndex = getRealIndex(
                pagerIndex = pagerState.currentPage,
                listSize = realPageCount,
                startIndex = startIndex,
                isInfinity = isInfinity
            )
            indicatorContent(
                pageState = pagerState,
                item = list[realIndex],
                pageIndex = realIndex
            )
        }
    }

    DisposableEffect(key1 = true) {
        onDispose {
//            Log.e("LEE", "HorizontalPagerCompose() DisposableEffect")
            job?.cancel()
            job = null
        }
    }
}

/**
 * 실제 아이템 Index 계산하는 함수
 */
private fun getRealIndex(
    pagerIndex : Int,
    listSize : Int,
    startIndex : Int,
    isInfinity : Boolean
) = if (isInfinity) {
    (pagerIndex - startIndex).floorMod(listSize)
} else {
    pagerIndex
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