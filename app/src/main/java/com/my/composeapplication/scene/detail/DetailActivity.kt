package com.my.composeapplication.scene.detail

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.NestedScrollCompose
import com.my.composeapplication.base.ScrollTopButtonCompose
import com.my.composeapplication.base.SwipeRefreshCompose
import com.my.composeapplication.scene.bmi.CustomTopAppBar
import com.my.composeapplication.scene.health.PagerIndicator
import com.my.composeapplication.viewmodel.DetailViewModel
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by YourName on 2022/09/14.
 */
@AndroidEntryPoint
class DetailActivity : BaseComponentActivity() {
    private val viewModel : DetailViewModel by viewModels()
    override fun getContent() : @Composable () -> Unit = {
        DetailScreen()
    }
}

@Composable
fun DetailScreen() {
    val scrollState = rememberLazyListState()
    SwipeRefreshViewHoisting {
        NestedScrollCompose(
            topAppbar = { height, offsetHeightPx ->
                DetailAppbar(height, offsetHeightPx)
            },
            toolbarHeight = 64.dp,
            toolbarFixedHeight = 30.dp,
            fixedContainer = { height, offsetHeightPx ->
                HorizontalMenuView(height, offsetHeightPx)
            },
            foldingAnimation = true
        ) { paddingValue, setOffset ->
            ScrollTopButtonCompose(scrollState, setOffset) {
                LazyColumn(
                    state = scrollState,
                    contentPadding = PaddingValues(top = paddingValue),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        HeaderPagerView()
                        DescriptionView()
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeRefreshViewHoisting(content : @Composable () -> Unit) {
    val viewModel : DetailViewModel = viewModel(LocalContext.current as BaseComponentActivity)
    val isRefresh by viewModel.isRefreshing.collectAsState()

    SwipeRefreshCompose(
        isRefresh,
        onRefresh = {
            viewModel.onRefresh()
        },
    ) {
        content()
    }
}

@Composable
fun DetailAppbar(height : Dp, offsetHeightPx : Int) {
    CustomTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .offset { IntOffset(x = 0, y = offsetHeightPx) },
        title = "Detail",
        showBack = true
    )
}

@Composable
fun HorizontalMenuView(height : Dp, offsetHeightPx : Int = Int.MAX_VALUE) {
    Text(
        text = "HorizontalMenuView",
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .offset { IntOffset(x = 0, y = offsetHeightPx) }
            .background(Color.Green)
            .border(1.dp, Color.Black)
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HeaderPagerView() {
    val viewModel : DetailViewModel = viewModel(LocalContext.current as BaseComponentActivity)
    val list = viewModel._horizontalPagerItems
    val state = rememberPagerState()
    val contentScale = if (
        LocalConfiguration.current.screenWidthDp / LocalConfiguration.current.screenHeightDp < 1.0f
    ) {
        ContentScale.FillWidth
    } else {
        ContentScale.FillHeight
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            count = list.size,
            state = state,
        ) { page ->
            val item = list[page]
            GlideImage(
                imageModel = item.imageUrl,
                modifier = Modifier.clickable {
                    Log.e("LEE", "HealthPagerItem() name: ${item.title}")
                },
                contentScale = contentScale
            )
        }
        if (list.isNotEmpty()) {
            PagerIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                pagerState = state
            )
        }
    }
}

@Composable
fun DescriptionView() {
    Column {
        FirstMenu()
        SecondMenu()
        ThirdMenu()
    }
}

@Composable
fun FirstMenu() {
    Text(
        text = "FirstMenu",
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .border(1.dp, Color.Black)
    )
}

@Composable
fun SecondMenu() {
    Text(
        text = "SecondMenu",
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .border(1.dp, Color.Black)
    )
}

@Composable
fun ThirdMenu() {
    Text(
        text = "ThirdMenu",
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .border(1.dp, Color.Black)
    )
}

@Preview(name = "DetailScreen")
@Composable
fun DetailScreenPreview() {
    DetailScreen()
}