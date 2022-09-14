package com.my.composeapplication.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.NestedScrollCompose

/**
 * Created by YourName on 2022/08/19.
 */
class NestedScrollActivity : BaseComponentActivity() {
    override fun getContent() : @Composable () -> Unit = {
        NestedScrollScreen()
    }
}

@Composable
fun NestedScrollScreen() {
    NestedScrollCompose(
        toolbarHeight = 110.dp,
        topAppbar = { height, offsetPx ->
            NestedTopBar(height, offsetPx)
        },
        toolbarFixedHeight = 70.dp,
        fixedContainer = { height, offsetPx ->
            NestedFixedScreen(height, offsetPx)
        }
    ) { paddingValue, setOffset ->
        LazyColumn(contentPadding = PaddingValues(top = paddingValue)) {
            items(100) { index ->
                Text(
                    "I'm item $index", modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Green)
                        .padding(16.dp),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun NestedTopBar(height : Dp, offsetPx : Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .offset { IntOffset(x = 0, y = offsetPx) }
    ) {
        val titleHeight = 70.dp
        TopAppBar(
            modifier = Modifier
                .height(titleHeight),
            title = { Text("toolbar offset is ${offsetPx}") }
        )
        Box(
            modifier = Modifier
                .height(height - titleHeight)
                .background(Color.Magenta)
        ) {
            Text(text = "heheheheh")
        }
    }

}

@Composable
fun NestedFixedScreen(height : Dp, offsetPx : Int) {
    Text(
        text = "Fixed Title",
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .offset { IntOffset(x = 0, y = offsetPx) }
            .background(Color.Red)
    )
}