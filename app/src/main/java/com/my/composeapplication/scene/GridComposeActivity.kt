package com.my.composeapplication.scene

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.my.composeapplication.data.SampleData
import java.util.*

/**
 * Created by YourName on 2022/07/01.
 * gird layout
 */
class GridComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        val composeView = ComposeView(this).apply {
            this.setContent {
                GridBodyContent()
            }
        }
        setContentView(composeView)
    }
}

@Composable
fun GridBodyContent(data: List<String> = SampleData.nameSample, modifier : Modifier = Modifier, rows : Int = 3) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        StaggerGrid(modifier, rows = rows) {
            data.forEach {
                Chip(modifier = Modifier.padding(8.dp), text = it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chip(modifier : Modifier = Modifier, text : String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .size(16.dp)
                .background(color = MaterialTheme.colorScheme.secondary))
            Spacer(Modifier.width(4.dp))
            Text(text = text, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun StaggerGrid(
    modifier : Modifier = Modifier,
    rows : Int = 3,
    content : @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Keep track of the width of each row
        val rowWidths = IntArray(rows) { 0 }

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows) { 0 }

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.mapIndexed { index, measurable ->

            // Measure each child
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)

            placeable
        }

        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // Grid's height is the sum of the tallest element of each row
        // coerced to the height constraints
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // Y of each row, based on the height accumulation of previous rows
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }

        layout(width, height) {
            // x cord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@Preview
@Composable
fun previewGrid() {
    GridBodyContent()
}

@Preview
@Composable
fun previewChip() {
    Chip(Modifier, "aaa")
}