package com.my.composeapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Column {
        Row() {
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
        Row() {
            val constraintSet = buttonTextConstraintSet()
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

private fun buttonTextConstraintSet():ConstraintSet {
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

@Preview
@Composable
fun ConstraintLayoutPreview() {
    ConstraintLayoutContent()
}