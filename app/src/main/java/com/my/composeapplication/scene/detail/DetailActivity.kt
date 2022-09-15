package com.my.composeapplication.scene.detail

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.my.composeapplication.R
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.NestedScrollCompose
import com.my.composeapplication.base.ScrollTopButtonCompose
import com.my.composeapplication.base.SwipeRefreshCompose
import com.my.composeapplication.scene.bmi.CustomTopAppBar
import com.my.composeapplication.scene.health.PagerIndicator
import com.my.composeapplication.scene.health.data.PagerItem
import com.my.composeapplication.viewmodel.DetailViewModel
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by YourName on 2022/09/14.
 */
@AndroidEntryPoint
class DetailActivity : BaseComponentActivity() {
    private val viewModel : DetailViewModel by viewModels()
    override fun getContent() : @Composable () -> Unit = {
        DetailScreenHoisting()
    }
}

private val toolbarHeight = 64.dp

sealed class DetailMenuEnum(val title : String, val index : Int) {
    object Pager : DetailMenuEnum("Pager", 0)
    object SecretMenu : DetailMenuEnum("SecretMenu", 1)
    object FirstContent : DetailMenuEnum("FirstContent", 2)
    object SecondContent : DetailMenuEnum("SecondContent", 3)
    object ThirdContent : DetailMenuEnum("ThirdContent", 4)
}

@Composable
fun DetailScreenHoisting() {
    val lazyListState = rememberLazyListState()
    val itemPositionMap = rememberSaveable {
        mutableMapOf(Pair(0, 0))
    }
    var toolbarOffset by rememberSaveable {
        mutableStateOf(0)
    }
    DetailScreen(
        lazyListState,
        itemPositionMap,
        toolbarOffset,
        storeOffset = {
            toolbarOffset = it
        }
    )
}

@Composable
fun DetailScreen(
    lazyListState : LazyListState,
    itemPositionMap : MutableMap<Int, Int>,
    initOffset: Int,
    storeOffset: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val toolbarHeightPx = LocalDensity.current.run {
        toolbarHeight.roundToPx()
    }
    SwipeRefreshViewHoisting {
        NestedScrollCompose(
            initOffset = initOffset.toFloat(),
            topAppbar = { height, offsetHeightPx ->
                storeOffset(offsetHeightPx)
                DetailAppbar(height, offsetHeightPx)
            },
            toolbarHeight = toolbarHeight,
            toolbarFixedHeight = 40.dp,
            fixedContainer = { height, offsetHeightPx ->
                HorizontalMenuComposeHoisting(height, offsetHeightPx) { index ->
                    coroutineScope.launch {
                        var offset = toolbarHeightPx
                        for (i in 0 until index) {
                            offset += itemPositionMap[i] ?: 0
                        }
//                        Log.e("LEE", "HealthPagerItem() name: ${itemPositionMap} offset: $offset")
                        lazyListState.animateScrollToItem(DetailMenuEnum.Pager.index, offset)
                    }
                }
            },
            foldingAnimation = true
        ) { paddingValue, setOffset ->
            ScrollTopButtonCompose(lazyListState, setOffset) {
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(top = paddingValue),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        HeaderPagerComposeHoisting(
                            itemPositionMap = itemPositionMap
                        )
                    }
                    item {
                        DescriptionView(itemPositionMap)
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
    val activity = (LocalContext.current as BaseComponentActivity)
    CustomTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .offset { IntOffset(x = 0, y = offsetHeightPx) },
        title = "Detail",
        showBack = true,
        onBack = {
            activity.finish()
        }
    )
}

@Composable
fun HorizontalMenuComposeHoisting(
    height : Dp,
    offsetHeightPx : Int = Int.MAX_VALUE,
    setScrollPosition : (Int) -> Unit
) {
    val menuList = listOf(DetailMenuEnum.FirstContent, DetailMenuEnum.SecondContent, DetailMenuEnum.ThirdContent)
    val listState = rememberLazyListState()
    HorizontalMenuCompose(
        height,
        offsetHeightPx,
        menuList,
        menuLazyListState = listState,
        setScrollPosition = setScrollPosition
    )
}

@Composable
fun HorizontalMenuCompose(
    height : Dp,
    offsetHeightPx : Int = Int.MAX_VALUE,
    menuList : List<DetailMenuEnum>,
    menuLazyListState : LazyListState,
    setScrollPosition : (Int) -> Unit
) {
    LazyRow(
        state = menuLazyListState,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .offset { IntOffset(x = 0, y = offsetHeightPx) }
            .background(Color.Green)
            .border(1.dp, Color.Black)
    ) {
        items(menuList) {
            Box(modifier = Modifier
                .clickable {
                    setScrollPosition(it.index)
                }
                .fillMaxHeight()
                .border(1.dp, Color.Black, RectangleShape)
                .padding(horizontal = 5.dp)) {
                Text(
                    text = it.title,
                    modifier = Modifier
                        .align(Alignment.Center),
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HeaderPagerComposeHoisting(
    viewModel : DetailViewModel = viewModel(LocalContext.current as BaseComponentActivity),
    itemPositionMap : MutableMap<Int, Int>
) {
    val list = viewModel._horizontalPagerItems
    val state = rememberPagerState()
    val contentScale = if (
        LocalConfiguration.current.screenWidthDp / LocalConfiguration.current.screenHeightDp < 1.0f
    ) {
        ContentScale.FillWidth
    } else {
        ContentScale.FillHeight
    }
    HeaderPagerCompose(
        list,
        state,
        contentScale,
        itemPositionMap,
        itemIndex = DetailMenuEnum.Pager.index
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HeaderPagerCompose(
    list : List<PagerItem>,
    state : PagerState,
    contentScale : ContentScale,
    itemPositionMap : MutableMap<Int, Int>,
    itemIndex : Int = 0
) {
    Box(
        modifier = Modifier
            .onGloballyPositioned {
                if (it.isAttached) {
                    itemPositionMap[itemIndex] = it.size.height
                }
            }
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
                contentScale = contentScale,
                previewPlaceholder = R.drawable.ic_launcher_foreground
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
fun DescriptionView(itemPositionMap : MutableMap<Int, Int>) {
    Column {
        FirstMenu(itemPositionMap, DetailMenuEnum.FirstContent)
        SecondMenu(itemPositionMap, DetailMenuEnum.SecondContent)
        ThirdMenu(itemPositionMap, DetailMenuEnum.ThirdContent)
    }
}

@Composable
fun FirstMenu(
    itemPositionMap : MutableMap<Int, Int>,
    menuItem : DetailMenuEnum,
) {
    Text(
        text = menuItem.title,
        modifier = Modifier
            .onGloballyPositioned {
                if (it.isAttached) {
                    itemPositionMap[menuItem.index] = it.size.height
                }
            }
            .fillMaxWidth()
            .height(500.dp)
            .border(1.dp, Color.Black)
    )
}

@Composable
fun SecondMenu(
    itemPositionMap : MutableMap<Int, Int>,
    menuItem : DetailMenuEnum,
) {
    Text(
        text = menuItem.title,
        modifier = Modifier
            .onGloballyPositioned {
                if (it.isAttached) {
                    itemPositionMap[menuItem.index] = it.size.height
                }
            }
            .fillMaxWidth()
            .height(500.dp)
            .border(1.dp, Color.Black)
    )
}

@Composable
fun ThirdMenu(
    itemPositionMap : MutableMap<Int, Int>,
    menuItem : DetailMenuEnum,
) {
    Text(
        text = menuItem.title,
        modifier = Modifier
            .onGloballyPositioned {
                if (it.isAttached) {
                    itemPositionMap[menuItem.index] = it.size.height
                }
            }
            .fillMaxWidth()
            .height(500.dp)
            .border(1.dp, Color.Black)
    )
}

@Preview(name = "HorizontalMenu")
@Composable
fun HorizontalMenuPreview() {
    HorizontalMenuCompose(
        height = 70.dp,
        menuLazyListState = rememberLazyListState(),
        setScrollPosition = {},
        menuList = listOf(DetailMenuEnum.FirstContent, DetailMenuEnum.SecondContent, DetailMenuEnum.ThirdContent)
    )
}

@Preview(name = "DetailScreen")
@Composable
fun DetailScreenPreview() {
//    val scrollState = rememberLazyListState()
//    ScrollTopButtonCompose(scrollState, {}) {
//        LazyColumn(
//            state = scrollState,
//            contentPadding = PaddingValues(top = 0.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            item {
//                HeaderPagerComposeHoisting(DetailViewModel())
//            }
//            item {
//                DescriptionView()
//            }
//        }
//    }
}