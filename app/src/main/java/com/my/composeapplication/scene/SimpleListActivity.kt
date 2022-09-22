package com.my.composeapplication.scene

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.my.composeapplication.R
import com.my.composeapplication.base.CustomTopAppBar
import com.my.composeapplication.base.NestedScrollCompose
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by YourName on 2022/06/30.
 * List의 scrollState를 이용한 버튼 노출 및 스크롤 이동 샘픔
 */
class SimpleListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        val composeView = ComposeView(this).apply {
            this.setContent {
                SimpleList()
            }
        }
        setContentView(composeView)
    }

    override fun onBackPressed() {
        finish()
    }
}

@Composable
fun SimpleList() {
    val listSize = 100
// We save the scrolling position with this state
    val scrollState = rememberLazyListState()
// We save the coroutine scope where our animated scroll will be executed
    val coroutineScore = rememberCoroutineScope()
    val showButton by remember {
        derivedStateOf {
            val visiableInfo = scrollState.layoutInfo.visibleItemsInfo
            if (visiableInfo.isNotEmpty()) {
                Log.e(SimpleListActivity::class.simpleName, "SimpleList() firstIndex: ${visiableInfo.first().index}")
            }
            scrollState.firstVisibleItemIndex > 0
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .height(0.dp)
                .weight(1.0f)
        ) {
            NestedScrollCompose(
                toolbarHeight = 64.dp,
                topAppbar = { height, offsetPx ->
                    CustomTopAppBar(title = "BIM Calculator", modifier = Modifier
                        .height(height)
                        .offset { IntOffset(x = 0, y = offsetPx) })
                }) { paddingValue, setOffset ->
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    LazyColumn(
                        state = scrollState,
                    ) {
                        items(listSize) { item ->
                            ImageListItem(item)
                        }
                    }
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        this@Row.AnimatedVisibility(visible = showButton) {
                            IconButton(onClick = {
                                coroutineScore.launch { scrollState.animateScrollToItem(0) }
                            }) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
        Row(modifier = Modifier.height(50.dp)) {
            ButtonContainer(coroutineScore, scrollState, listSize)
        }
    }
}

@Composable
fun ButtonContainer(coroutineScore : CoroutineScope, scrollState : LazyListState, listSize : Int) {
    Button(onClick = {
        coroutineScore.launch {
            scrollState.animateScrollToItem(0)
        }
    }) {
        Text(text = "Scroll to the top")
    }
    Button(onClick = {
        coroutineScore.launch {
            scrollState.animateScrollToItem(listSize)
        }
    }) {
        Text(text = "Scroll to the end")
    }
}

@Composable
private fun ImageListItem(index : Int) {
    var itemState by rememberSaveable {
        mutableStateOf(false)
    }
    var fbState by rememberSaveable {
        mutableStateOf(false)
    }
    val onClick : (Boolean) -> Unit = {
        itemState = !it
    }
    ImageListItem(
        index = index,
        itemState = itemState,
        fbState = fbState,
        click = onClick
    ) {
        fbState = !it
    }
}

@Composable
fun ImageListItem(
    index : Int,
    itemState : Boolean,
    fbState : Boolean,
    modifier : Modifier = Modifier,
    click : (Boolean) -> Unit,
    fbClick : (Boolean) -> Unit
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .clickable {
            click(itemState)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (itemState) {
                        Color.Gray
                    } else {
                        Color.White
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            //TODO Preview Mode의 error로 무시하도록 처리함.
            if (!LocalInspectionMode.current) {
                GlideImage(
                    imageModel = "https://developer.android.com/images/brand/Android_Robot.png",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Item #$index",
                    style = MaterialTheme.typography.displayMedium
                )
                IconButton(
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .align(alignment = Alignment.CenterEnd),
                    onClick = { fbClick(fbState) }
                ) {
                    Icon(
                        imageVector = if (fbState) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = "favorite",
                        tint = Color.Black
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun SimpleListPreview() {
    SimpleList()
}