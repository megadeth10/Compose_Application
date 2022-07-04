package com.my.composeapplication

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.data.SampleData

/**
 * Created by YourName on 2022/07/04.
 */
class LazyComposeActivity : BaseComponentActivity() {
    override fun getContent() : @Composable () -> Unit = {

        LazyComposeContent(SampleData.nameSample)
    }
}

@Composable
fun LazyComposeContent(list : List<String>) {
    LazyVerticalGrid(columns = GridCells.Adaptive(120.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        item(span= { GridItemSpan(1) }) {
            NamePickerItem("HEAD", modifier = Modifier.width(200.dp)) {
                Log.e(ConversationActivity::class.simpleName, "name click($it)")
            }
        }
        var state = false
        items(list, span= { GridItemSpan(if(it.length % 2 == 0) 2 else 1) }) { item ->
            state = !state
            Log.e(LazyComposeActivity::class.simpleName, "state: $state, isOdd: ${item.length % 2}")
            NamePickerItem(item, modifier = Modifier.width(200.dp)) {
                Log.e(LazyComposeActivity::class.simpleName, "name click($it)")
            }
        }

        item(span= { GridItemSpan(1) }) {
            NamePickerItem("Bottom", modifier = Modifier.width(200.dp)) {
                Log.e(ConversationActivity::class.simpleName, "name click($it)")
            }
        }
    }
}

@Preview(widthDp = 360)
@Composable
fun LazyComposePreview(){
    LazyComposeContent(SampleData.nameSample)
}
