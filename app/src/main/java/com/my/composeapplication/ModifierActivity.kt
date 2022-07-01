package com.my.composeapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.data.SampleData

/**
 * Created by YourName on 2022/07/01.
 */
class ModifierActivity : BaseComponentActivity() {
    override fun getContent() : @Composable () -> Unit = { ModifierContent() }
}

@Composable
fun ModifierContent(list : List<String> = SampleData.nameSample) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SmallTopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ModifierActivity",
                        maxLines = 2,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            modifier = Modifier.height(50.dp),
        )
        Row(
            modifier = Modifier
                .background(color = Color.LightGray)
                .size(200.dp)
                .padding(16.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            StaggerGrid {
                list.forEach {
                    Chip(modifier = Modifier.padding(8.dp), text = it)
                }
            }
        }
    }
}

@Preview(
    widthDp = 360
)
@Composable
fun ModifierPreview() {
    ModifierContent()
//    Column {
//        Row(verticalAlignment = CenterVertically, modifier = Modifier.height(70.dp)) {
//            Text(
//                text = "ModifierActivity",
//                modifier = Modifier.fillMaxWidth().background(Color.Red),
//                maxLines = 2,
//
//            )
//        }
//        Row {
//            Text(
//                text = "ModifierActivity",
//                modifier = Modifier.fillMaxWidth().height(70.dp),
//                textAlign = TextAlign.Center,
//                maxLines = 2
//            )
//        }
//    }
}