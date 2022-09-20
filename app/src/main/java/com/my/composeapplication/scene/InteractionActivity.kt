package com.my.composeapplication.scene

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.util.Size
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.my.composeapplication.R
import com.my.composeapplication.base.BaseComponentActivity

/**
 * InteractionSource를 이용한 샘플코드
 */
class InteractionActivity : BaseComponentActivity() {
    override fun getContent(): @Composable () -> Unit = {
//        InteractionScreenHoisting()
//        InteractionScreen2()
//        TextAnimationComponent()
//        MeasuringScaleComponent()
        ZoomableComposable()
    }
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun TextAnimationComponent() {
    // Annotate string is used to define a text with multiple styles.
    val text = buildAnnotatedString {
        // appended string
        append("Jetpack ")
        // Add inline content. Inline content is used to describe/tag sections inside the Text
        // composable that we will replace by other composables tagged with the same id. For
        // example, we add two sections with id's "composeLogo" & "animatedText" below. We will
        // reference the same id's in the inlineContent map that we will be passing to the Text
        // Commposable.
        appendInlineContent("composeLogo", "Compose Logo")
        appendInlineContent("animatedText", "Animated Text")
    }

    val inlineContent = mapOf(
        "composeLogo" to InlineTextContent(
            placeholder = Placeholder(
                width = 2.em,
                height = 1.em,
                placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
            ),
            children = {
                ComposeLogoComponent()
            }
        ),
        "animatedText" to InlineTextContent(
            placeholder = Placeholder(
                width = 5.em,
                height = 35.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
            ),
            children = {
                ColorChangingTextComponent()
            }
        )
    )
    // Column is a composable that places its children in a vertical sequence. You
    // can think of it similar to a LinearLayout with the vertical orientation.
    // In addition we also pass a few modifiers to it.

    // You can think of Modifiers as implementations of the decorators pattern that are used to
    // modify the composable that its applied to. In the example below, we configure the
    // Box to occupy the entire available height & width using the Modifier.fillMaxSize() modifier.
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Text is a predefined composable that does exactly what you'd expect it to - display text
        // on the screen. It allows you to customize its appearance using style, fontWeight,
        // fontSize, etc.

        // In addition, we also pass it the inlineContent map that is used to describe how we
        // will specify alternate composables to describe areas within the Text composable.
        Text(
            text = text,
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 35.sp
            ),
            inlineContent = inlineContent
        )
    }
}

fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap? {
    val bitmap = ContextCompat.getDrawable(context, drawableId)?.let {
        (DrawableCompat.wrap(it)).mutate()
    }?.let {
        val bitmap = Bitmap.createBitmap(
            it.intrinsicWidth,
            it.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
//        val canvas = androidx.compose.ui.graphics.Canvas(bitmap)
        val canvas = android.graphics.Canvas(bitmap)
        it.setBounds(0, 0, canvas.width, canvas.height)
        it.draw(canvas)
        bitmap
    }

    return bitmap
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun ComposeLogoComponent() {
    // There are multiple methods available to load an image resource in Compose. However, it would
    // be advisable to use the painterResource method as it loads an image resource asynchronously

    val image = getBitmapFromVectorDrawable(
        context = LocalContext.current,
        drawableId = R.drawable.ic_launcher_foreground
    )

    // rememberInfiniteTransition is used to create a transition that uses infitine
    // child animations. Animations typically get invoked as soon as they enter the
    // composition so don't need to be explicitly started.
    val infiniteTransition = rememberInfiniteTransition()
    // Create a value that is altered by the transition based on the configuration. We use
    // the animated float value the returns and updates a float from the initial value to
    // target value and repeats it (as its called on the infititeTransition).
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween<Float>(
                durationMillis = 3000,
                easing = FastOutLinearInEasing,
            ),
        )
    )

    // You can think of Modifiers as implementations of the decorators pattern that are
    // used to modify the composable that its applied to. In this example, we configure the
    // Image composable to have a height of 48 dp.
    image?.let {
        Canvas(Modifier.size(48.dp)) {
            // As the Transition is changing the interpolating the value of the animated float
            // "rotation", you get access to all the values including the intermediate values as
            // its  being updated. The value of "rotation" goes from 0 to 360 and transitions
            // infinitely due to the infiniteRepetable animationSpec used above.
            rotate(rotation) {
                drawImage(it.asImageBitmap())
            }
        }
    }
}


// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun ColorChangingTextComponent() {
    // Reacting to state changes is the core behavior of Compose. You will notice a couple new
    // keywords that are compose related - remember & mutableStateOf.remember{} is a helper
    // composable that calculates the value passed to it only during the first composition. It then
    // returns the same value for every subsequent composition. Next, you can think of
    // mutableStateOf as an observable value where updates to this variable will redraw all
    // the composable functions that access it. We don't need to explicitly subscribe at all. Any
    // composable that reads its value will be recomposed any time the value
    // changes. This ensures that only the composables that depend on this will be redraw while the
    // rest remain unchanged. This ensures efficiency and is a performance optimization. It
    // is inspired from existing frameworks like React.
    val currentColor by remember { mutableStateOf(Color.Red) }
    val transition = updateTransition(currentColor, label = "aaa")

    val color by transition.animateColor(label = "aaa") { state ->
        when (state) {
            Color.Red -> Color.Green
            Color.Green -> Color.Blue
            Color.Blue -> Color.Red
            else -> Color.Red
        }
    }

    // As the Transition is changing the interpolating the value of your props based
    // on the "from state" and the "to state", you get access to all the values
    // including the intermediate values as they are being updated. We can use the
    // state variable and access the relevant props/properties to update the relevant
    // composables/layouts. Below, we use state[color] to get get the latest value of color
    // and use it to set the color of the Text composable.
    Text(
        text = "Compose",
        color = color,
        style = androidx.compose.ui.text.TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 35.sp
        )
    )
}

@Composable
fun InteractionScreen2() {
// Hoist the MutableInteractionSource that we will provide to interactions
    val interactionSource = remember { MutableInteractionSource() }

// Provide the MutableInteractionSource instances to the interactions we want to observe state
// changes for
    val draggable = Modifier.draggable(
        interactionSource = interactionSource,
        orientation = Orientation.Horizontal,
        state = rememberDraggableState { /* update some business state here */ }
    )

    val clickable = Modifier.clickable(
        interactionSource = interactionSource,
        // This component is a compound component where part of it is clickable and part of it is
        // draggable. As a result we want to show indication for the _whole_ component, and not
        // just for clickable area. We set `null` indication here and provide an explicit
        // Modifier.indication instance later that will draw indication for the whole component.
        indication = null
    ) { /* update some business state here */ }

// SnapshotStateList we will use to track incoming Interactions in the order they are emitted
    val interactions = remember { mutableStateListOf<Interaction>() }

// Collect Interactions - if they are new, add them to `interactions`. If they represent stop /
// cancel events for existing Interactions, remove them from `interactions` so it will only
// contain currently active `interactions`.
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> interactions.add(interaction)
                is PressInteraction.Release -> interactions.remove(interaction.press)
                is PressInteraction.Cancel -> interactions.remove(interaction.press)
                is DragInteraction.Start -> interactions.add(interaction)
                is DragInteraction.Stop -> interactions.remove(interaction.start)
                is DragInteraction.Cancel -> interactions.remove(interaction.start)
            }
        }
    }

// Display some text based on the most recent Interaction stored in `interactions`
    val text = when (interactions.lastOrNull()) {
        is DragInteraction.Start -> "Dragged"
        is PressInteraction.Press -> "Pressed"
        else -> "No state"
    }

    Column(
        Modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        Row(
            // Draw indication for the whole component, based on the Interactions dispatched by
            // our hoisted MutableInteractionSource
            Modifier.indication(
                interactionSource = interactionSource,
                indication = LocalIndication.current
            )
        ) {
            Box(
                Modifier
                    .size(width = 240.dp, height = 80.dp)
                    .then(clickable)
                    .border(BorderStroke(3.dp, Color.Blue))
                    .padding(3.dp)
            ) {
                val pressed = interactions.any { it is PressInteraction.Press }
                Text(
                    text = if (pressed) "Pressed" else "Not pressed",
                    style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            }
            Box(
                Modifier
                    .size(width = 240.dp, height = 80.dp)
                    .then(draggable)
                    .border(BorderStroke(3.dp, Color.Red))
                    .padding(3.dp)
            ) {
                val dragged = interactions.any { it is DragInteraction.Start }
                Text(
                    text = if (dragged) "Dragged" else "Not dragged",
                    style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            }
        }
        Text(
            text = text,
            style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
}

@Composable
fun InteractionScreen() {
    // Hoist the MutableInteractionSource that we will provide to interactions
    val interactionSource = remember { MutableInteractionSource() }

// Provide the MutableInteractionSource instances to the interactions we want to observe state
// changes for
    val draggable = Modifier.draggable(
        interactionSource = interactionSource,
        orientation = Orientation.Vertical,
        state = rememberDraggableState { /* update some business state here */ }
    )

    val clickable = Modifier.clickable(
        interactionSource = interactionSource,
        // This component is a compound component where part of it is clickable and part of it is
        // draggable. As a result we want to show indication for the _whole_ component, and not
        // just for clickable area. We set `null` indication here and provide an explicit
        // Modifier.indication instance later that will draw indication for the whole component.
        indication = null
    ) { /* update some business state here */ }

// SnapshotStateList we will use to track incoming Interactions in the order they are emitted
    val interactions = remember { mutableStateListOf<Interaction>() }

// Collect Interactions - if they are new, add them to `interactions`. If they represent stop /
// cancel events for existing Interactions, remove them from `interactions` so it will only
// contain currently active `interactions`.
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> interactions.add(interaction)
                is PressInteraction.Release -> interactions.remove(interaction.press)
                is PressInteraction.Cancel -> interactions.remove(interaction.press)
                is DragInteraction.Start -> interactions.add(interaction)
                is DragInteraction.Stop -> interactions.remove(interaction.start)
                is DragInteraction.Cancel -> interactions.remove(interaction.start)
            }
        }
    }

// Display some text based on the most recent Interaction stored in `interactions`
    val text = when (interactions.lastOrNull()) {
        is DragInteraction.Start -> "Dragged"
        is PressInteraction.Press -> "Pressed"
        else -> "No state"
    }

    Column(
        Modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        Row(
            // Draw indication for the whole component, based on the Interactions dispatched by
            // our hoisted MutableInteractionSource
            Modifier.indication(
                interactionSource = interactionSource,
                indication = LocalIndication.current
            )
        ) {
            Box(
                Modifier
                    .size(width = 240.dp, height = 80.dp)
                    .then(clickable)
                    .border(BorderStroke(3.dp, Color.Blue))
                    .padding(3.dp)
            ) {
                val pressed = interactions.any { it is PressInteraction.Press }
                Text(
                    text = if (pressed) "Pressed" else "Not pressed",
                    style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            }
            Box(
                Modifier
                    .size(width = 240.dp, height = 80.dp)
                    .then(draggable)
                    .border(BorderStroke(3.dp, Color.Red))
                    .padding(3.dp)
            ) {
                val dragged = interactions.any { it is DragInteraction.Start }
                Text(
                    text = if (dragged) "Dragged" else "Not dragged",
                    style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            }
        }
        Text(
            text = text,
            style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
}

@Composable
fun InteractionScreenHoisting() {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val dragState = interactionSource.collectIsDraggedAsState()
    val pressState = interactionSource.collectIsPressedAsState()

    InteractionScreen(
        interactionSource,
        dragState,
        pressState
    )
}

@Composable
fun InteractionScreen(
    interactionSource: MutableInteractionSource = remember {
        MutableInteractionSource()
    },
    dragState: State<Boolean> = interactionSource.collectIsDraggedAsState(),
    pressState: State<Boolean> = interactionSource.collectIsPressedAsState()
) {
    val draggable = Modifier.draggable(
        state = rememberDraggableState {},
        interactionSource = interactionSource,
        orientation = Orientation.Horizontal
    )
    val clickable = Modifier.clickable(
        interactionSource = interactionSource,
        indication = LocalIndication.current
    ) {}
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .then(draggable)
                .then(clickable)
                .fillMaxWidth()
                .weight(2f)
        ) {}
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (dragState.value) "Drag" else "not drag",
                modifier = Modifier.background(
                    if (pressState.value)
                        Color.Red
                    else
                        Color.White
                )
            )
        }
    }
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun MeasuringScaleComponent() {
    // We create a ScrollState that's "remember"ed  to add proper support for a scrollable component.
    // This allows us to also control the scroll position and other scroll related properties.

    // remember calculates the value passed to it only during the first composition. It then
    // returns the same value for every subsequent composition. More details are available in the
    // comments below.
    val scrollState = rememberScrollState()

    // Row is a composable that places its children in a horizontal sequence. You
    // can think of it similar to a LinearLayout with the horizontal orientation.

    // You can think of Modifiers as implementations of the decorators pattern that are
    // used to modify the composable that its applied to. In this example, we assign a
    // padding of 16dp and specify it to occupy the entire available width.

    // In addition, we make use of the horizontalScroll modifier. This modifier makes the using
    // composable to have scroll functionality in the horizontal direction.
    val range: ClosedRange<Int> = -20..1020
    Box {
        Column(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
                content = {
                    for (i in range.start..range.endInclusive) {
                        ScaleLineComponent(i)
                    }
                })
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                for (i in range.start..range.endInclusive) {
                    ScaleLineNumber(i, range)
                }
            }
        }

        // Column is a composable that places its children in a vertical sequence. You
        // can think of it similar to a LinearLayout with the vertical orientation.
        // In addition we also pass a few modifiers to it.
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            ScaleCenterPointer()
        }
    }
}

@Composable
fun ScaleLineNumber(index: Int, range: ClosedRange<Int>) {
    val widthPx = LocalDensity.current.run {
        130.dp.roundToPx()
    }
    val isDivisibleBy10 = isDivide(index)
    if (isDivisibleBy10) {
        var floatDestruction = 0.dp
        when (index) {
            range.start -> {

            }
            range.endInclusive -> {

            }
            else -> {
                // dp to pixel로 인한 소수점 소실 값을 보정하는 역활
                if(index % 10 == 0) {
                    floatDestruction = 1.dp
                }
            }
        }
        val onSurfaceColor = MaterialTheme.colorScheme.onSurface
        Text(
            // Adding an empty string to ensure that that it also gets the background color
            // assigned otherwise it results in bad looking UI.
            text = "${index / 10}",
            textAlign = TextAlign.Start,
            style = TextStyle(fontFamily = FontFamily.Monospace),
            color = onSurfaceColor,
            modifier = Modifier
                .width(130.dp + floatDestruction)
        )
    }
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun ScaleLineComponent(index: Int) {
    val isDivisibleBy10 = isDivide(index)
    // Surface color from the color palette specified by the applied Theme. In our case, its
    // what we specify in the CustomTheme composable.
    val surfaceColor = MaterialTheme.colorScheme.surface
    // The color configured for rendering content on top of surfaces that use the surface color.
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    // Column is a composable that places its children in a vertical sequence. You
    // can think of it similar to a LinearLayout with the vertical orientation. We also give it a
    // modifier.

    // You can think of Modifiers as implementations of the decorators pattern that are
    // used to modify the composable that its applied to. In this example, we add background
    // color to the Column using the drawBackground(color) modifier.
    Column(
        modifier = Modifier
            .background(color = surfaceColor)
    ) {
        // We use the Canvas composable that gives you access to a canvas that you can draw
        // into. We also pass it a modifier.
        Canvas(
            modifier = Modifier
                .padding(5.dp)
                .width(3.dp)
                .height(100.dp)
        ) {
            // Allows you to draw a line between two points (p1 & p2) on the canvas.
            drawLine(
                color = onSurfaceColor,
                start = Offset(0f, 0f),
                end = Offset(0f, if (isDivisibleBy10) size.height else size.height * 0.2f),
                strokeWidth = if (isDivisibleBy10) size.width else size.width * 0.3f
            )
        }
        // Text is a predefined composable that does exactly what you'd expect it to - display text
        // on the screen. It allows you to customize its appearance using style, fontWeight,
        // fontSize, etc.
    }
}

private fun isDivide(index: Int) = index % 10 == 0

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun ScaleCenterPointer() {
    // Primary color from the color palette specified by the applied Theme. In our case, its
    // what we specify in the CustomTheme composable.
    val primaryColor = MaterialTheme.colorScheme.primary
    // Column is a composable that places its children in a vertical sequence. You
    // can think of it similar to a LinearLayout with the vertical orientation.
    Column {
        // We use the Canvas composable that gives you access to a canvas that you can draw
        // into. We also pass it a modifier.

        // You can think of Modifiers as implementations of the decorators pattern that are
        // used to modify the composable that its applied to. In this example, we give it a
        // padding of 5 dp, height of 120dp & width of 3dp.
        Canvas(
            modifier = Modifier
                .padding(5.dp)
                .height(120.dp)
                .width(3.dp)
        ) {
            // Allows you to draw a line between two points (p1 & p2) on the canvas.
            drawLine(
                color = primaryColor,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = size.width
            )
        }
    }
}

/**
 * 아래 center 기준 Zoom 코드에서 center 기준 edge 체크하는 코드
 */
fun checkMinMaxFromCenterPoint(offset : Offset, imageSize : Size, scale : Float, screenSize : Size) : Offset? {
    val x = offset.x
    val y = offset.y
    val screenCenterX = screenSize.width / 2
    val screenCenterY = screenSize.height / 2
    val minX = (screenCenterX / scale - screenCenterX) * scale
    val minY = (screenCenterY / scale - screenCenterY) * scale
    val maxX = -minX
    val maxY = -minY
    val checkX : Float? = if (x < minX) {
        minX
    } else if (x > maxX) {
        maxX
    } else {
        null
    }

    val checkY : Float? = if (y < minY) {
        minY
    } else if (y > maxY) {
        maxY
    } else {
        null
    }
    val checkOffset : Offset? = if (checkX != null || checkY != null) {
        Offset(
            x = checkX ?: x,
            y = checkY ?: y,
        )
    } else {
        null
    }
    Log.e("LEE", "checkMinMax() offset: $offset scale: $scale")
    Log.e("LEE", "checkMinMax() checkOffset: $checkOffset ")
    return checkOffset
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun ZoomableComposable() {
    // Reacting to state changes is the core behavior of Compose. We use the state composable
    // that is used for holding a state value in this composable for representing the current
    // value scale(for zooming in the image) & translation(for panning across the image). Any
    // composable that reads the value of counter will be recomposed any time the value changes.
    // This ensures that only the composables that depend on this will be redraw while the
    // rest remain unchanged. This ensures efficiency and is a performance optimization. It
    // is inspired from existing frameworks like React.
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // Column is a composable that places its children in a vertical sequence. You
    // can think of it similar to a LinearLayout with the vertical orientation.
    // In addition we also pass a few modifiers to it.

    // You can think of Modifiers as implementations of the decorators pattern that are used to
    // modify the composable that its applied to. In the example below, we make the Column composable
    // zoomable by leveraging the Modifier.pointerInput modifier
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        awaitFirstDown()
                        do {
                            val event = awaitPointerEvent()
                            scale *= event.calculateZoom()
                            val offset = event.calculatePan()
                            offsetX += offset.x
                            offsetY += offset.y
                        } while (event.changes.any { it.pressed })
                    }
                }
            }
    ) {
        // There are multiple methods available to load an image resource in Compose.
        // However, it would be advisable to use the painterResource method as it loads
        // an image resource asynchronously
        val imagepainter = painterResource(id = R.drawable.landscape)
        // Image is a pre-defined composable that lays out and draws a given [ImageBitmap].
        // We use the graphicsLayer modifier to modify the scale & translation of the image.
        // This is read from the state properties that we created above.
        Image(
            modifier = Modifier.fillMaxSize().graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            ),
            painter = imagepainter,
            contentDescription = "Landscape Image"
        )
    }
}

/**
 * Android Studio lets you preview your composable functions within the IDE itself, instead of
 * needing to download the app to an Android device or emulator. This is a fantastic feature as you
 * can preview all your custom components(read composable functions) from the comforts of the IDE.
 * The main restriction is, the composable function must not take any parameters. If your composable
 * function requires a parameter, you can simply wrap your component inside another composable
 * function that doesn't take any parameters and call your composable function with the appropriate
 * params. Also, don't forget to annotate it with @Preview & @Composable annotations.
 */
@Preview(name = "ZoomableComposable")
@Composable
fun ZoomableComposablePreview() {
    ZoomableComposable()
}

@Preview(name = "MeasuringScaleComponent", backgroundColor = 0xffffff)
@Composable
fun MeasuringScaleComponentPreview() {
    MeasuringScaleComponent()
}

@Preview(name = "InteractionScreen")
@Composable
fun InteractionScreenPreview() {
    InteractionScreen()
}
