package com.my.composeapplication.scene.health

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Pentagon
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.MeasureUnconstrainedViewWidth
import com.my.composeapplication.base.OnEndItem
import com.my.composeapplication.base.customScaffold
import com.my.composeapplication.scene.SimpleListActivity
import com.my.composeapplication.scene.bmi.CustomTopAppBar
import com.my.composeapplication.scene.health.data.PagerItem
import com.my.composeapplication.scene.health.data.TodoItem
import com.my.composeapplication.viewmodel.HealthViewModel
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by YourName on 2022/08/22.
 */
@AndroidEntryPoint
class HealthActivity : BaseComponentActivity() {
    private val viewModel by viewModels<HealthViewModel>()
    override fun getContent() : @Composable () -> Unit = {
        TodoListHoisting()
    }
}

class TodoListState(
    val viewModel : HealthViewModel,
    val allCheckState : MutableState<Boolean>,
    val scrollState : LazyListState
)

@Composable
fun rememberTodoListState(
    viewModel : HealthViewModel = viewModel(
        LocalContext.current as BaseComponentActivity
    ),
    allCheckState : MutableState<Boolean> = rememberSaveable {
        mutableStateOf(false)
    },
    scrollState : LazyListState = rememberLazyListState()
) = remember(viewModel, allCheckState, scrollState) {
    TodoListState(
        viewModel = viewModel,
        allCheckState = allCheckState,
        scrollState = scrollState
    )
}

@Composable
private fun TodoListHoisting() {
    val todoListState = rememberTodoListState()
    var allCheckState by todoListState.allCheckState

    val onCheck : (TodoItem, Boolean) -> Unit = { item, isCheck ->
        todoListState.viewModel.setCheck(item, isCheck)
    }
    val onRemove : (TodoItem) -> Unit = { item ->
        todoListState.viewModel.removeItem(item)
    }

    val onCheckAll : () -> Unit = {
        val nextState = !allCheckState
        todoListState.viewModel.allCheck(nextState)
        allCheckState = nextState
    }

    val context = (LocalContext.current as BaseComponentActivity).baseContext
    val goActivity : () -> Unit = {
        context.startActivity(Intent(context, SimpleListActivity::class.java))
    }

    // TODO 해당 키가 변경될때만 하위 Scope를 실행함. Recomposition되어도 실행되지 않음, 컴포지션이 삭제되면 해당 Scope는 취소됨.
    LaunchedEffect(key1 = true) {
        Log.e(HealthActivity::class.simpleName, "TodoListHoisting() LaunchedEffect")
    }

    customScaffold(
        topAppbar = {
            CustomTopAppBar(
                title = "Checkable List",
                actions = {
                    IconButton(onClick = onCheckAll) {
                        Icon(
                            imageVector = if (allCheckState)
                                Icons.Default.Undo
                            else
                                Icons.Default.DoneAll,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = goActivity) {
                        Icon(
                            imageVector = Icons.Default.Pentagon,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            SwipeRefreshView(todoListState) {
                TodoList(
                    todoListState,
                    onCheck,
                    onRemove,
                )
            }
        }
    }
}

@Composable
fun SwipeRefreshView(
    todoListState : TodoListState,
    content : @Composable () -> Unit
) {
    val isRefresh by todoListState.viewModel.isRefreshing.collectAsState()
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefresh),
        onRefresh = {
            todoListState.viewModel.onRefresh()
        },
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

@Composable
fun HeaderPagerHoisting(todoListState : TodoListState, modifier : Modifier = Modifier) {
    val list = todoListState.viewModel.horizontalPagerItems
    HeaderPager(
        modifier = modifier,
        list = list,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HeaderPager(
    modifier : Modifier = Modifier,
    list : List<PagerItem>
) {
    val state = rememberPagerState()

    val title by remember {
        derivedStateOf {
            list.getOrNull(state.currentPage)?.title ?: ""
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            HealthPager(
                list = list,
                pagerState = state,
            )
//            InfinityHorizontalPager(list = list) { modifier, item, page ->
//                HealthPagerItem(item = item)
//            }
            if (title.isNotEmpty()) {
                PagerTitle(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    title = title
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
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HealthPager(
    modifier : Modifier = Modifier,
    list : List<PagerItem>,
    pagerState : PagerState,
) {
    HorizontalPager(
        modifier = modifier,
        count = list.size,
        state = pagerState,
    ) { page ->
        if (list.size > page) {
            val item = list[page]
            HealthPagerItem(
                item = item
            )
        }
    }
}

@Composable
fun HealthPagerItem(
    modifier : Modifier = Modifier,
    item : PagerItem,
) {
    val contentScale = if (
        LocalConfiguration.current.screenWidthDp / LocalConfiguration.current.screenHeightDp < 1.0f
    ) {
        ContentScale.FillWidth
    } else {
        ContentScale.FillHeight
    }
    GlideImage(
        imageModel = item.imageUrl,
        modifier = modifier,
        contentScale = contentScale
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerIndicator(modifier : Modifier = Modifier, pagerState : PagerState) {
    HorizontalPagerIndicator(
        pagerState = pagerState,
        modifier = modifier
            .padding(16.dp),
        activeColor = Color.White,
        indicatorShape = CircleShape.copy(

        )
    )
}

@Composable
fun PagerTitle(modifier : Modifier = Modifier, title : String) {
    Box(modifier = modifier) {
        MeasureUnconstrainedViewWidth(
            viewToMeasure = {
                DisplayText(title = title)
            }
        ) { measuredWidth ->
            val (animationState, changeAnimationState) = remember {
                mutableStateOf(false)
            }
            var displayText by remember {
                mutableStateOf(title)
            }
            if (displayText != title) {
                changeAnimationState(true)
                displayText = title
            }
            val color by animateColorAsState(
                targetValue = if (animationState) Color.Red else Color.Gray,
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = 0,
                    easing = LinearEasing
                ),
                finishedListener = {
                    changeAnimationState(false)
                }
            )
//        val offset by animateDpAsState(
//            targetValue = if (animationState) measuredWidth else 0.dp,
//            animationSpec = tween(
//                durationMillis = 300,
//                delayMillis = 0,
//                easing = LinearEasing
//            ),
//            finishedListener = {
//            }
//        )
            Box(modifier = Modifier.width(measuredWidth)) {
                val width = with(LocalDensity.current) {
                    measuredWidth.roundToPx()
                }
                AnimatedVisibility(
                    visible = !animationState,
                    exit = slideOutHorizontally { width },
                ) {
                    DisplayText(
                        displayText,
                        color
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayText(
    title : String,
    color : Color,
) {
    DisplayText(
        modifier = Modifier
            .background(color.copy(alpha = .3f)),
        title = title
    )
}

@Composable
fun DisplayText(
    modifier : Modifier = Modifier,
    title : String,
) {
    Text(
        text = title,
        modifier = modifier
            .padding(5.dp),
        color = Color.White,
    )
}

@Composable
fun TodoList(
    todoListState : TodoListState,
    onChecked : (item : TodoItem, state : Boolean) -> Unit,
    onClose : (item : TodoItem) -> Unit,
    modifier : Modifier = Modifier
) {
    val list = todoListState.viewModel.list
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = todoListState.scrollState
    ) {
        item {
            HeaderPagerHoisting(todoListState)
        }
        items(list) { item ->
            TodoItemView(
                item = item,
                onChecked = { isChecked ->
                    onChecked(item, isChecked)
                },
                onClose = onClose
            )
        }
        if (list.isNotEmpty()) {
            item {
                BottomProgress()
            }
        }
    }
    todoListState.scrollState.OnEndItem {
        todoListState.viewModel.moreList()
    }
}

@Composable
fun BottomProgress(modifier : Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(30.dp),
            color = Color.Black
        )
    }
}

@Composable
fun TodoItemView(
    item : TodoItem,
    onChecked : (state : Boolean) -> Unit,
    onClose : (item : TodoItem) -> Unit,
    modifier : Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.Black))
            .padding(
                horizontal = 10.dp, vertical = 5.dp
            )
    ) {
        Text(
            text = item.text,
            modifier = Modifier
                .height(30.dp)
                .weight(1f),
            maxLines = 1,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(10.dp))
        Checkbox(
            checked = item.isChecked, onCheckedChange = onChecked,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            onClick = { onClose(item) },
            modifier = Modifier.size(30.dp)
        ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = null)
        }
    }
}


@Preview(name = "TextLayout")
@Composable
fun TextLayoutPreview() {
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(200.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Text(
            text = "aaaaaa",
            modifier = Modifier
                .background(Color.Gray.copy(alpha = .3f))
                .padding(5.dp)
        )
    }
}

@Preview(name = "HeaderPager")
@Composable
fun HeaderPagerPreview() {
    HeaderPager(
        list = listOf(
            PagerItem(
                "https://cdn.pixabay.com/photo/2020/07/14/16/18/snow-5404785_960_720.jpg",
                " 산이다."
            )
        ),
    )
}

@Preview(name = "BottomProgress")
@Composable
fun BottomProgressPreview() {
    BottomProgress()
}

@Preview(name = "TodoItemView")
@Composable
fun TodoItemViewPreview() {
    TodoItemView(TodoItem(1, "xxxx"), {}, {})
}