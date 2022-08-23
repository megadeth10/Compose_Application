package com.my.composeapplication.scene

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.my.composeapplication.base.BaseComponentActivity

/**
 * Created by YourName on 2022/07/01.
 * ConstraintLayout 과 ConstraintSet 이용한 Compose View 관계 배치
 */
class ConstraintLayoutActivity : BaseComponentActivity() {
    override fun getContent() : @Composable () -> Unit = {
        ConstraintLayoutContent()
    }
}

@Composable
fun ConstraintLayoutContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ConstraintLayout {
                val (button1, button2, text1) = createRefs()

                Button(
                    onClick = {},
                    modifier = Modifier.constrainAs(button1) {
                        top.linkTo(parent.top, margin = 16.dp)
                    }
                ) {
                    Text("Button1")
                }

                Text(
                    text = "Text",
                    modifier = Modifier.constrainAs(text1) {
                        top.linkTo(button1.bottom, margin = 16.dp)
                        centerAround(button1.end)
                    }
                )
                val barrier = createEndBarrier(button1, text1)
                Button(
                    onClick = {},
                    modifier = Modifier.constrainAs(button2) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(barrier)
                    }
                ) {
                    Text("Button2")
                }
            }
        }
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            Log.e(ConstraintLayoutActivity::class.simpleName, "ConstraintLayoutContent() minwidth: $minWidth")
            // TODO Compose의 width를 이용하여 compose의 배치를 자유롭게 변경한다.
            val constraintSet = if (minWidth < 600.dp) {
                portraitConstraintSet()
            } else {
                landScapeConstraintSet()
            }
            Row() {
                ConstraintLayout(constraintSet) {
                    Button(
                        onClick = {},
                        modifier = Modifier.layoutId("button3")
                    ) {
                        Text("Button1")
                    }
                    Text(
                        text = "Text",
                        modifier = Modifier.layoutId("text2")
                    )
                    Button(
                        onClick = {},
                        modifier = Modifier.layoutId("button4")
                    ) {
                        Text("Button2")
                    }
                }
            }
        }
    }
}

private fun portraitConstraintSet() : ConstraintSet {
    return ConstraintSet {
        val button3 = createRefFor("button3")
        val button4 = createRefFor("button4")
        val text2 = createRefFor("text2")

        constrain(button3) {
            top.linkTo(parent.top, margin = 16.dp)
        }

        val barrier = createEndBarrier(button3, text2)
        constrain(text2) {
            top.linkTo(button3.bottom, margin = 16.dp)
            centerAround(button3.end)
        }
        constrain(button4) {
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(barrier)
        }
    }
}

private fun landScapeConstraintSet() : ConstraintSet {
    return ConstraintSet {
        val button3 = createRefFor("button3")
        val button4 = createRefFor("button4")
        val text2 = createRefFor("text2")

        constrain(button3) {
            top.linkTo(parent.top, margin = 16.dp)
        }
        constrain(button4) {
            top.linkTo(button3.top)
            start.linkTo(button3.end)
        }
        constrain(text2) {
            top.linkTo(button4.top)
            start.linkTo(button4.end)
            end.linkTo(parent.end)
        }
    }
}

@Composable
fun TwoTexts(
    text1: String,
    text2: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        // TODO lineHeight를 이용하여 lineSpacing을 해결할수 있다.
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.End)
                .wrapContentHeight(Alignment.CenterVertically),
            text = text1,
            lineHeight = 20.sp,
            fontSize = 20.sp,
            textAlign = TextAlign.End
        )
        Divider(
            color = Color.Black,
            modifier = Modifier.fillMaxHeight().width(1.dp)
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),
            text = text2
        )
    }
}

@Preview
@Composable
fun TwoTextsPreview() {
    MaterialTheme {
        Surface {
            TwoTexts(text1 = "Hi\nasfasdfasdf\nasdfasdf", text2 = "there")
        }
    }
}

@Preview
@Composable
fun ConstraintLayoutPreview() {
    ConstraintLayoutContent()
}

@Preview
@Composable
fun testPreview() {
    Text(text = "aaaaa")
}