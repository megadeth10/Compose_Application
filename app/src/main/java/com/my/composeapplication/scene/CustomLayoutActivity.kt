package com.my.composeapplication.scene

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.my.composeapplication.ui.theme.Black
import com.my.composeapplication.ui.theme.ComposeApplicationTheme

/**
 * Created by YourName on 2022/07/01.
 *  Layout 과 layout을 이용한 Custom layout
 */
class CustomLayoutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        val composeView = ComposeView(this).apply {
            this.setContent {
                BodyContent()
            }
        }

        setContentView(composeView)
    }
}

@Composable
fun BodyContent() {
    Column {
        Row {
            CustomContent()
        }
        Row {
            NormalContent()
        }
    }
}

@Composable
fun NormalContent() {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .border(1.dp, Black, RectangleShape)
    ) {
        Text("NormalContent")
        Text("Hi there!", Modifier.padding(top = 32.dp))
        Column(modifier = Modifier.padding(8.dp)) {
            TextContent(modifier = Modifier.border(1.dp, Color.Red, RectangleShape))
        }
    }
}

@Composable
fun CustomContent() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .border(1.dp, Black, RectangleShape)
            .verticalScroll(scrollState)
    ) {
        Text("CustomContent")
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
        CustomStreamLayout(
            modifier = Modifier
                .wrapContentHeight()
                .padding(8.dp)
        ) {
            TextContent(modifier = Modifier.border(1.dp, Color.Red, RectangleShape))
        }
    }
}

@Composable
fun TextContent(modifier : Modifier = Modifier) {
    Text("MyOwnColumn", modifier)
    Text("places items", modifier)
    Text("vertically.", modifier)
    Text("We've done it by hand!", modifier)
    Text("MyOwnColumn", modifier)
    Text("places items", modifier)
    Text("vertically.", modifier)
    Text("We've done it by hand!", modifier)
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop : Dp
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints) // 한번만 측정

        // Check the composable has a first baseline
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        // Height of the composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY // 맞춤 높이를 제공함.
        layout(placeable.width, height) {
            // Where the composable gets placed
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Composable
fun CustomStreamLayout(
    modifier : Modifier = Modifier,
    // custom layout attributes
    content : @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each child
            measurable.measure(constraints)
        }

        var height = 0
        var width = 0
        placeables.forEach { placeable ->
            width += placeable.width
            if (height == 0) {
                height += placeable.height
            }
            if (constraints.maxWidth < width) {
                width = placeable.width
                height += placeable.height
            }
        }

        var yPosition = 0
        var xPosition = 0
        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, height) {
            // Track the y co-ord we have placed children up to
            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                if (constraints.maxWidth < xPosition + placeable.width) {
                    // Record the y co-ord placed up to
                    xPosition = 0
                    yPosition += placeable.height
                }
                placeable.placeRelative(x = xPosition, y = yPosition)

                xPosition += placeable.width
            }
        }
    }
}

@Composable
fun CustomLayout(
    modifier : Modifier = Modifier,
    // custom layout attributes
    content : @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each child
            measurable.measure(constraints)
        }

        var height = 0
        placeables.forEach { placeable ->
            height += placeable.height
        }
        // Track the y co-ord we have placed children up to
        var yPosition = 0
        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, height) {
            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}

@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
    ComposeApplicationTheme {
        CustomContent()
    }
}

@Preview
@Composable
fun TextWithNormalPaddingPreview() {
    ComposeApplicationTheme {
        NormalContent()
    }
}