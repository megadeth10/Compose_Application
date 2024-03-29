package com.my.composeapplication.scene.detail

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.my.composeapplication.R
import com.my.composeapplication.base.*
import com.my.composeapplication.scene.health.PagerIndicator
import com.my.composeapplication.scene.health.data.PagerItem
import com.my.composeapplication.viewmodel.DetailViewModel
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by YourName on 2022/09/14.
 * TODO
 * 1. 메뉴를 pager 아래에서 나오는 Compose 만들어 보기
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
    object EmptyContent : DetailMenuEnum("EmptyContent", 5)

    companion object {
        fun getMenuItem(index : Int) = when (index) {
            Pager.index -> Pager
            SecretMenu.index -> SecretMenu
            FirstContent.index -> FirstContent
            SecondContent.index -> SecondContent
            ThirdContent.index -> ThirdContent
            EmptyContent.index -> EmptyContent
            else -> null
        }
    }
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
        },
    )
}

@Composable
fun DetailScreen(
    lazyListState : LazyListState,
    itemPositionMap : MutableMap<Int, Int>,
    initOffset : Int,
    storeOffset : (Int) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val toolbarHeightPx = LocalDensity.current.run {
        toolbarHeight.roundToPx()
    }
    val menuHighlightItem = remember {
        derivedStateOf {
            val currentIndex = lazyListState.firstVisibleItemIndex
            Log.e("LEE", "DetailScreen() currentIndex:$currentIndex")
            DetailMenuEnum.getMenuItem(currentIndex)
        }
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
                HorizontalMenuComposeHoisting(
                    height,
                    offsetHeightPx,
                    setScrollPosition = { index ->
                        coroutineScope.launch {
//                            var offset = toolbarHeightPx
//                            for (i in 0 until index) {
//                                offset += itemPositionMap[i] ?: 0
//                            }
//                        Log.e("LEE", "HealthPagerItem() index: $index map: ${itemPositionMap} offset: $offset")
                            lazyListState.animateScrollToItem(index, toolbarHeightPx)
                        }
                    },
                    menuHighlight = menuHighlightItem.value,
                )
            },
            foldingAnimation = true
        ) { paddingValue, setOffset ->
            LazyListScrollTopButtonCompose(lazyListState, setOffset) {
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
//                        DescriptionView(itemPositionMap)
                    }
                    item {
                        FirstMenu(itemPositionMap, DetailMenuEnum.FirstContent)
                    }
                    item {
                        SecondMenu(itemPositionMap, DetailMenuEnum.SecondContent)
                    }
                    item {
                        ThirdMenu(itemPositionMap, DetailMenuEnum.ThirdContent)
                    }
                    item {
                        EmptyMenu(itemPositionMap, DetailMenuEnum.EmptyContent)
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
    setScrollPosition : (Int) -> Unit,
    menuHighlight : DetailMenuEnum?,
) {
    val menuList = listOf(DetailMenuEnum.FirstContent, DetailMenuEnum.SecondContent, DetailMenuEnum.ThirdContent)
    val listState = rememberLazyListState()
    HorizontalMenuCompose(
        height,
        offsetHeightPx,
        menuList,
        menuLazyListState = listState,
        setScrollPosition = setScrollPosition,
        menuHighlight = menuHighlight,
    )
}

@Composable
fun HorizontalMenuCompose(
    height : Dp,
    offsetHeightPx : Int = Int.MAX_VALUE,
    menuList : List<DetailMenuEnum>,
    menuLazyListState : LazyListState,
    setScrollPosition : (Int) -> Unit,
    menuHighlight : DetailMenuEnum?,

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
                    Log.e("LEE", "HorizontalMenuCompose() click index: ${it.index}")
                    if (it.index != menuHighlight?.index) {
                        setScrollPosition(it.index)
                    }
                }
                .fillMaxHeight()
                .border(1.dp, Color.Black, RectangleShape)
                .padding(horizontal = 5.dp)) {
                Text(
                    text = it.title,
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontWeight = if (menuHighlight == it) FontWeight.Bold else FontWeight.Normal
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
    val list = viewModel.horizontalPagerItems
    val contentScale = if (
        LocalConfiguration.current.screenWidthDp / LocalConfiguration.current.screenHeightDp < 1.0f
    ) {
        ContentScale.FillWidth
    } else {
        ContentScale.FillHeight
    }
    HeaderPagerCompose(
        list,
        contentScale,
        itemPositionMap,
        itemIndex = DetailMenuEnum.Pager.index,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HeaderPagerCompose(
    list : List<PagerItem>,
    contentScale : ContentScale,
    itemPositionMap : MutableMap<Int, Int>,
    itemIndex : Int = 0,
    autoScroll : Boolean = false,
) {
    HorizontalPagerCompose(
        modifier = Modifier
            .onGloballyPositioned {
                if (it.isAttached) {
                    itemPositionMap[itemIndex] = it.size.height
                }
            }
            .fillMaxWidth()
            .height(190.dp),
        list = list,
        autoScroll = autoScroll,
        indicatorContent = { pagerState, item, pageIndex ->
            PagerIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                pagerState = pagerState
            )
        }
    ) { interactionSource, item, pageIndex ->
        GlideImage(
            imageModel = item.imageUrl,
            modifier = Modifier
                .clickable(interactionSource = interactionSource, indication = null) {
                    Log.e("LEE", "HealthPagerItem() name: ${item.title}")
                },
            contentScale = contentScale,
            previewPlaceholder = R.drawable.ic_launcher_foreground
        )
    }
}

@Composable
fun FirstMenu(
    itemPositionMap : MutableMap<Int, Int>,
    menuItem : DetailMenuEnum,
    viewModel:DetailViewModel = viewModel(LocalContext.current as BaseComponentActivity)
) {
    val itemSize = viewModel.item.collectAsState()
    Column(
        modifier = Modifier
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
                .border(1.dp, Color.Black)
                .height(50.dp)

        )
//        LazyColumn(
//        ) {
//            items(20) {
              repeat(itemSize.value) {
                  Text(
                      text = menuItem.title + it,
                      modifier = Modifier
                          .fillMaxWidth()
                          .border(1.dp, Color.Black)
                          .height(50.dp)
                  )
              }
            }
//        }
//    }
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

/**
 * 최 하단에 dummy를 추가하여 menu와 스크롤 위치를 어느정도 동기화 하기위한 눈속임의 용도
 */
@Composable
fun EmptyMenu(
    itemPositionMap : MutableMap<Int, Int>,
    menuItem : DetailMenuEnum,
) {
    val height = LocalConfiguration.current.screenHeightDp.dp.times(0.3f)
    Text(
        text = menuItem.title,
        modifier = Modifier
            .onGloballyPositioned {
                if (it.isAttached) {
                    itemPositionMap[menuItem.index] = it.size.height
                }
            }
            .fillMaxWidth()
            .height(height)
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
        menuList = listOf(DetailMenuEnum.FirstContent, DetailMenuEnum.SecondContent, DetailMenuEnum.ThirdContent),
        menuHighlight = null,
//        storeMenuHighlight = {}
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