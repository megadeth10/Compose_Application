package com.my.composeapplication.scene.seekbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.my.composeapplication.base.BaseComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

class SeekbarActivity : BaseComponentActivity() {
    override fun getContent(): @Composable () -> Unit = {
        Column {
            SliderComposeHoisting()
            RangeSliderComposeHoisting()
        }
    }
}

@Composable
fun SliderComposeHoisting() {
    var sliderPosition by remember {
        mutableStateOf(0f)
    }

    SliderValueCompose(
        sliderPosition = sliderPosition,
        setSliderPosition = {
            sliderPosition = it
        }
    )
}

@Composable
fun SliderValueCompose(
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
            sliderPosition,
            setSliderPosition,
            steps,
            padding
        )
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun SliderCompose(
    sliderPosition: Float,
    setSliderPosition: (Float) -> Unit,
    steps: Int = 10,
    padding: Dp = 0.dp
) {
    val widthDp = LocalConfiguration.current.screenWidthDp.dp.minus(padding * 2)
    val itemWidthDp = 20.dp
    val dividePoint = steps - 1
    val itemCount = steps + 1
    val offsetDp = (widthDp.minus(itemWidthDp * (itemCount))).div(steps)

    Box(
        contentAlignment = Alignment.BottomCenter,
    ) {
        Slider(
            modifier = Modifier.background(Color.Gray),
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
                Text(
                    text = i.toString(),
                    modifier = Modifier.width(itemWidthDp),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(10f, TextUnitType.Sp)
                )
                Spacer(modifier = Modifier.width(offsetDp))
            }
        }
    }
}

@Composable
fun RangeSliderComposeHoisting() {
    val initValue = 0f .. 1f
    var sliderPosition by remember {
        mutableStateOf(initValue)
    }

    RangeValueCompose(
        sliderPosition = sliderPosition,
        setSliderPosition = {
            sliderPosition = it
        },
        valueRange = initValue
    )
}

@Composable
fun RangeValueCompose(
    sliderPosition: ClosedFloatingPointRange<Float>,
    setSliderPosition: (ClosedFloatingPointRange<Float>) -> Unit,
    steps: Int = 10,
    padding: Dp = 0.dp,
    valueRange : ClosedFloatingPointRange<Float> = 0f .. 1f
) {
    Column {
        Text(
            text = "${sliderPosition.start} ~ ${sliderPosition.endInclusive}"
        )
        RangeSliderCompose(
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
    sliderPosition: ClosedFloatingPointRange<Float>,
    setSliderPosition: (ClosedFloatingPointRange<Float>) -> Unit,
    steps: Int = 10,
    padding: Dp = 0.dp,
    valueRange : ClosedFloatingPointRange<Float> = 0f .. 1f
) {
    val widthDp = LocalConfiguration.current.screenWidthDp.dp.minus(padding * 2)
    val itemWidthDp = 20.dp
    val dividePoint = steps - 1
    val itemCount = steps + 1
    val offsetDp = (widthDp.minus(itemWidthDp * (itemCount))).div(steps)

    Box(
        contentAlignment = Alignment.BottomCenter,
    ) {
        RangeSlider(
            modifier = Modifier.background(Color.Gray),
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
                Text(
                    text = i.toString(),
                    modifier = Modifier.width(itemWidthDp),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(10f, TextUnitType.Sp)
                )
                Spacer(modifier = Modifier.width(offsetDp))
            }
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