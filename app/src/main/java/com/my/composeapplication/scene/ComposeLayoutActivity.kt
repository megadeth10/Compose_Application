package com.my.composeapplication.scene

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.my.composeapplication.R
import com.my.composeapplication.ui.theme.Black
import com.my.composeapplication.ui.theme.ComposeApplicationTheme
import com.my.composeapplication.ui.theme.RED_POINT
import com.my.composeapplication.viewmodel.ComposeLayoutViewModel

/**
 * Created by YourName on 2022/06/30.
 * TopAppBar, BottomAppBar 테스트 코드
 */
class ComposeLayoutActivity : ComponentActivity() {
    private val composeLayoutViewModel by viewModels<ComposeLayoutViewModel>()
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        val composeView = ComposeView(this).apply {
            this.setContent {
                LayoutsCodelab(composeLayoutViewModel)
            }
        }
        setContentView(composeView)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutsCodelab(composeLayoutViewModel : ComposeLayoutViewModel) {
    Log.e("ComposeLayoutActivity", "LayoutsCodelab()")
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "LayoutsCodelab")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "first tap icon")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(50.dp), contentPadding = PaddingValues(0.dp)) {
                LayoutBottomAppBar(composeLayoutViewModel)
            }
        }
    ) { innerPadding ->
        LayoutsContent(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun LayoutBottomAppBar(composeLayoutViewModel : ComposeLayoutViewModel) {
    Log.e("ComposeLayoutActivity", "LayoutBottomAppBar()")
    val firstID = 0
    val secondID = 1
    Row {
        val state by composeLayoutViewModel.mutableMenuId.observeAsState()
        BottomBar(
            Icons.Filled.Favorite,
            contentDescription = "first tap icon",
            modifier = Modifier
                .weight(1.0f)
                .background(if (state == firstID) RED_POINT else Color.Gray)
        ) {
            composeLayoutViewModel.setMutableMenuId(0)
        }
        BottomBar(
            Icons.Filled.Search,
            contentDescription = "first tap icon",
            modifier = Modifier
                .weight(1.0f)
                .background(if (state == secondID) RED_POINT else Color.Gray)
        ) {
            composeLayoutViewModel.setMutableMenuId(1)
        }
    }
}

@Composable
fun BottomBar(image : ImageVector, contentDescription : String, modifier : Modifier, onClick : () -> Unit) {
    Log.e("ComposeLayoutActivity", "BottomBar()")
    IconButton(
        onClick = onClick, modifier = modifier.fillMaxHeight()
    ) {
        Icon(image, contentDescription = contentDescription)
    }
}

@Composable
fun LayoutsContent(modifier : Modifier) {
    Log.e("ComposeLayoutActivity", "LayoutsContent()")
    Column(modifier = modifier) {
        Text(text = "Hi there!")
        Text(text = "Thanks for going through the Layouts codelab")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoTopAppBar() {
    ComposeApplicationTheme {
        Column {
            SmallTopAppBar(
                title = {
                    Text("item1")
                },
                navigationIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "menu icon",
                        Modifier
                            .size(50.dp)
                            .clickable {
                                Log.e("PhotoTopAppBar", "Icon click")
                            })
                }
            )
            PhotographerCard()
        }
    }
}

@Composable
fun PhotographerCard(modifier : Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = { /* Ignoring onClick */ })
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "thumbnail image")
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            // LocalContentAlpha is defining opacity level of its children
            Text("3 minutes ago", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(name = "Light Theme", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark Theme", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LayoutPreview(composeLayoutViewModel : ComposeLayoutViewModel = viewModel()) {
    LayoutsCodelab(composeLayoutViewModel)
}

@Preview(name = "Light Theme", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark Theme", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PhotographerCardPreview() {
    PhotoTopAppBar()
}
