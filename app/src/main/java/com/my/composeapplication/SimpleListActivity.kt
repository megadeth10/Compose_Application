package com.my.composeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.height(0.dp).weight(1.0f)) {
            LazyColumn(state = scrollState) {
                items(listSize) { item ->
                    ImageListItem(item)
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
fun ImageListItem(index : Int) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = rememberAsyncImagePainter(model = "https://developer.android.com/images/brand/Android_Robot.png"),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text("Item #$index", style = MaterialTheme.typography.displayMedium)
    }
}

@Preview
@Composable
fun SimpleListPreview() {
    SimpleList()
}