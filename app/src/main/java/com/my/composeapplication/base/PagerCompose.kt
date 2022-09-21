package com.my.composeapplication.base

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.my.composeapplication.scene.health.data.PagerItem
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
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalPagerCompose(
    modifier : Modifier = Modifier,
    listSize : Int,
    pagerState : PagerState,
    autoScroll : Boolean = false,

    indicatorContent : @Composable BoxScope.() -> Unit,
    pagerContent : @Composable (
        interactionSource : MutableInteractionSource,
        pageIndex : Int
    ) -> Unit,
) {
    var job : Job? by remember {
        mutableStateOf(null)
    }
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(key1 = pagerState.currentPage) {
        job?.cancel()
        job = startPagerTimer(
            coroutineScope = coroutineScope,
            size = listSize,
            autoScroll = autoScroll,
            pagerState = pagerState,
        )
    }
    LaunchedEffect(interactionSource) {
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
                        size = listSize,
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
                .fillMaxSize(),
            count = listSize,
            state = pagerState,
        ) { pagerIndex ->
            pagerContent(interactionSource, pagerIndex)
        }
        if (listSize > 1) {
            indicatorContent()
        }
    }

    DisposableEffect(key1 = true) {
        onDispose {
            job?.cancel()
            job = null
        }
    }
}

/**
 * endless roll pager
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun <T> InfinityHorizontalPager(
    modifier : Modifier = Modifier,
    list : List<T>,
    pagerState : PagerState,
    content : @Composable (Modifier, T, Int) -> Unit
) {
    // Display 10 items
    val pageCount = list.size
    val maxSize = MaxIndex
    val startIndex = maxSize / 2
    HorizontalPager(
        // Set the raw page count to a really large number
        count = maxSize,
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .placeholder(
                visible = list.isEmpty(),
                color = Color.Gray,
                shape = RectangleShape,
                highlight = PlaceholderHighlight.shimmer(Color.White)
            )
    ) { index ->
        // We calculate the page from the given index
        var page = (index - startIndex).floorMod(pageCount)
        page = if (list.size > page) page else 0
        if (list.size > page) {
            val item = list[page]
            content(Modifier.fillMaxSize(), item, page)
        }
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