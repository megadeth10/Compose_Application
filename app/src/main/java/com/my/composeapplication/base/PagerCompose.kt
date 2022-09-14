package com.my.composeapplication.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

/**
 * Created by YourName on 2022/09/14.
 * View Pager Compose
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
    val maxSize = Int.MAX_VALUE
    val startIndex = maxSize / 2
    HorizontalPager(
        // Set the raw page count to a really large number
        count = maxSize,
        state = pagerState,
        modifier = modifier.fillMaxWidth().placeholder(
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