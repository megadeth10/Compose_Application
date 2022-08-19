package com.my.composeapplication.scene

import android.os.Bundle
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.my.composeapplication.base.customScaffold
import com.my.composeapplication.scene.bmi.CustomTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by YourName on 2022/06/30.
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
}

@Composable
fun SimpleList() {
    val listSize = 100
// We save the scrolling position with this state
    val scrollState = rememberLazyListState()
// We save the coroutine scope where our animated scroll will be executed
    val coroutineScore = rememberCoroutineScope()
    val showButton by remember {
        derivedStateOf { scrollState.firstVisibleItemIndex > 0 }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .height(0.dp)
                .weight(1.0f)
        ) {

            customScaffold(
                topAppbar = {
                    CustomTopAppBar("BIM Calculator")
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier.padding(top = it.calculateTopPadding())
                    ) {
                        items(listSize) { item ->
                            ImageListItem(item)
                        }
                    }
                    Box(contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
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

@OptIn(ExperimentalMaterial3Api::class)
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
            Image(
                painter = rememberAsyncImagePainter(model = "https://developer.android.com/images/brand/Android_Robot.png"),
                contentDescription = "Android Logo",
                modifier = Modifier.size(50.dp)
            )
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