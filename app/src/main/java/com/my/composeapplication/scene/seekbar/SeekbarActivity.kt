package com.my.composeapplication.scene.seekbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.my.composeapplication.base.BaseComponentActivity

/**
 * Slide bar 샘플 코드
 */
class SeekbarActivity : BaseComponentActivity() {
    override fun getContent(): @Composable () -> Unit = {
        Column {
            SliderComposeHoisting()
            RangeSliderComposeHoisting()
            TestShapeCompose()
        }
    }
}

@Composable
fun SliderComposeHoisting(
    modifier: Modifier = Modifier,
    initValue: Float = 0f
) {
    var sliderPosition by remember {
        mutableStateOf(initValue)
    }

    SliderValueCompose(
        modifier = modifier,
        sliderPosition = sliderPosition,
        setSliderPosition = {
            sliderPosition = it
        }
    )
}

@Composable
fun SliderValueCompose(
    modifier: Modifier = Modifier,
    sliderPosition: Float,
    setSliderPosition: (Float) -> Unit,
    steps: Int = 10,
    padding: Dp = 0.dp
) {
    Column {
        Text(
            text = sliderPosition.toString()
        )
        SliderCompose(
            modifier = modifier,
            sliderPosition = sliderPosition,
            setSliderPosition = setSliderPosition,
            steps = steps,
            padding = padding
        )
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun SliderCompose(
    modifier: Modifier = Modifier,
    sliderPosition: Float,
    setSliderPosition: (Float) -> Unit,
    steps: Int = 10,
    padding: Dp = 0.dp
) {
    val itemWidthDp = 20.dp
    val dividePoint = steps - 1
    val itemCount = steps + 1
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter,
    ) {
        val widthDp = maxWidth.minus(padding * 2)
        val offsetDp = (widthDp.minus(itemWidthDp * (itemCount))).div(steps)

        Slider(
            modifier = Modifier.background(Color.LightGray),
            value = sliderPosition,
            onValueChange = setSliderPosition,
            steps = dividePoint
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding),
            verticalAlignment = Alignment.Bottom
        ) {
            for (i in 0..itemCount) {
                val contourValue = i / steps.toFloat()
                val highLight = sliderPosition >= contourValue
                Text(
                    text = i.toString(),
                    modifier = Modifier.width(itemWidthDp),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(10f, TextUnitType.Sp),
                    color = if (highLight) {
                        Color.Black
                    } else {
                        Color.Gray
                    },
                )
                if (i != itemCount) {
                    Spacer(modifier = Modifier.width(offsetDp))
                }
            }
        }
    }
}

@Composable
fun RangeSliderComposeHoisting(
    modifier: Modifier = Modifier,
    initValue: ClosedFloatingPointRange<Float> = 0f..1f
) {
    var sliderPosition by remember {
        mutableStateOf(initValue)
    }

    RangeValueCompose(
        modifier = modifier,
        sliderPosition = sliderPosition,
        setSliderPosition = {
            sliderPosition = it
        },
        valueRange = initValue
    )
}

@Composable
fun RangeValueCompose(
    modifier: Modifier = Modifier,
    sliderPosition: ClosedFloatingPointRange<Float>,
    setSliderPosition: (ClosedFloatingPointRange<Float>) -> Unit,
    steps: Int = 10,
    padding: Dp = 0.dp,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    Column {
        Text(
            text = "${sliderPosition.start} ~ ${sliderPosition.endInclusive}"
        )
        RangeSliderCompose(
            modifier = modifier,
            sliderPosition,
            setSliderPosition,
            steps,
            padding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUnitApi::class)
@Composable
fun RangeSliderCompose(
    modifier: Modifier = Modifier,
    sliderPosition: ClosedFloatingPointRange<Float>,
    setSliderPosition: (ClosedFloatingPointRange<Float>) -> Unit,
    steps: Int = 10,
    padding: Dp = 0.dp,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    val itemWidthDp = 20.dp
    val dividePoint = steps - 1
    val itemCount = steps + 1

    BoxWithConstraints(
        modifier = modifier.padding(padding),
        contentAlignment = Alignment.BottomCenter,
    ) {
        val widthDp = maxWidth.minus(padding * 2)
        val offsetDp = (widthDp.minus(itemWidthDp * (itemCount))).div(steps)
        RangeSlider(
            modifier = Modifier.background(Color.LightGray),
            value = sliderPosition,
            onValueChange = setSliderPosition,
            valueRange = valueRange,
            steps = dividePoint
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding),
            verticalAlignment = Alignment.Bottom
        ) {
            for (i in 0..itemCount) {
                val contourValue = i / steps.toFloat()
                val startValue = sliderPosition.start <= contourValue
                val endValue = sliderPosition.endInclusive >= contourValue
                val highLight = startValue && endValue
                Text(
                    text = i.toString(),
                    modifier = Modifier.width(itemWidthDp),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(10f, TextUnitType.Sp),
                    color = if (highLight) {
                        Color.Black
                    } else {
                        Color.Gray
                    }
                )
                if (i != itemCount) {
                    Spacer(modifier = Modifier.width(offsetDp))
                }
            }
        }
    }
}

@Composable
fun TestShapeCompose() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(
                1.dp,
                Color.Black
            ),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Surface(
                modifier = Modifier
                    .width(100.dp)
                    .height(30.dp),
                shape = RoundedCornerShape(topStart = 24.dp),
                elevation = 5.dp
            ) {}
            Spacer(Modifier.height(5.dp))
            Surface(
                modifier = Modifier
                    .width(100.dp)
                    .height(30.dp),
                shape = CutCornerShape(10.dp, 0.dp, 0.dp, 0.dp),
                elevation = 5.dp
            ) {}
        }
    }
}

@Preview(name = "SliderCompose")
@Composable
fun SliderComposePreview() {
    SliderComposeHoisting()
}

@Preview(name = "RangeSliderCompose")
@Composable
fun RangeSliderComposePreview() {
    RangeSliderComposeHoisting()
}

@Preview(name = "ShapeTest")
@Composable
fun ShapeTestPreview() {
    TestShapeCompose()
}