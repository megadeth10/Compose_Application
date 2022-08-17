package com.my.composeapplication.scene

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.composeapplication.base.BaseComponentActivity

/**
 * Created by YourName on 2022/07/01.
 * IntrinsicSize 이용한 사전 높이 계산 방법
 */
class IntrinsicActivity: BaseComponentActivity() {
    override fun getContent() : @Composable () -> Unit = {
        IntrinsicContent()
    }
}

@Composable
fun IntrinsicContent(modifier: Modifier = Modifier) {
    Column {
        // IntrinsicSize.Min으로 하위 요소의 크기를 최소로 강제 한다.
        Row(modifier = modifier.height(IntrinsicSize.Min)) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .wrapContentWidth(Alignment.Start),
                text = "text1\nasadas"
            )

            Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
                    .wrapContentWidth(Alignment.End),
                text = "text2"
            )
        }
    }
}

@Preview
@Composable
fun IntrinsicContentPreview() {
    IntrinsicContent()
}