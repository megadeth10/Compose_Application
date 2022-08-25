package com.my.composeapplication.base

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.my.composeapplication.base.data.PagerStateParam
import com.my.composeapplication.base.data.ScrollStateParam

/**
 * Created by YourName on 2022/08/24.
 */

/**
 * Save scroll state on all time.
 * @param key value for comparing screen
 * @param scrollParam arguments for find different between equals screen
 * @param initialFirstVisibleItemIndex see [LazyListState.firstVisibleItemIndex]
 * @param initialFirstVisibleItemScrollOffset see [LazyListState.firstVisibleItemScrollOffset]
 */
@Composable
fun rememberForeverLazyListState(
    key : String = "",
    initialFirstVisibleItemIndex : Int = 0,
    initialFirstVisibleItemScrollOffset : Int = 0,
    scrollParam : ScrollStateParam? = null,
    onDispose : ((key : String, index : Int, offset : Int) -> Unit)? = null
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

@OptIn(ExperimentalPagerApi::class)
@Composable
@Deprecated(message = "필요 없는거 같음 rotation시에 자동으로 인덱스 유지 되는거 확인함.")
fun rememberForeverPagerState(
    key : String = "",
    initialFirstVisibleItemIndex : Int = 0,
    scrollParam : PagerStateParam? = null,
    onDispose : ((key : String, index : Int) -> Unit)? = null
) : PagerState {
    val scrollState = rememberSaveable(saver = PagerState.Saver) {
        var index = initialFirstVisibleItemIndex
        scrollParam?.let {
            index = it.index
        }
        PagerState(
            currentPage = index,
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            val lastIndex = scrollState.currentPage
            onDispose?.invoke(key, lastIndex)
        }
    }
    return scrollState
}