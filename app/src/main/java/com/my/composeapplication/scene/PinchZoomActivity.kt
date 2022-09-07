package com.my.composeapplication.scene

import android.graphics.drawable.Drawable
import android.util.Log
import android.util.Size
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.my.composeapplication.R
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.CustomScaffold
import com.my.composeapplication.scene.bmi.CustomTopAppBar
import com.my.composeapplication.viewmodel.PinchZoomViewModel
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.*
import kotlin.math.roundToInt

/**
 * Created by YourName on 2022/09/05.
 * ref: https://developer.android.com/reference/kotlin/androidx/compose/foundation/gestures/package-summary?authuser=19#(androidx.compose.ui.input.pointer.PointerInputScope).detectTransformGestures(kotlin.Boolean,kotlin.Function4)
 */

@AndroidEntryPoint
class PinchZoomActivity : BaseComponentActivity() {
    private val viewModel by viewModels<PinchZoomViewModel>()
    override fun getContent() : @Composable () -> Unit = {
        PinchZoomScreen()
    }

    override fun onDestroy() {
        stopJob()
        super.onDestroy()
    }
}

private var resetTransformAnimationJob : Job? = null
private var resetOffsetAnimationJob : Job? = null

/**
 * Coroutine job 취소
 */
private fun stopJob() {
    resetOffsetAnimationJob?.cancel()
    resetTransformAnimationJob?.cancel()
    resetOffsetAnimationJob = null
    resetTransformAnimationJob = null
}

@Composable
fun PinchZoomScreen() {
    CustomScaffold(
        modifier = Modifier
            .fillMaxSize(),
        topAppbar = {
            CustomTopAppBar(
                title = "PinchZoom",
            )
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val (pinchView, box1, box2) = createRefs()
            Box(
                modifier = Modifier
                    .constrainAs(pinchView) {
                        top.linkTo(box1.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(box2.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .width(200.dp)
                    .height(200.dp)
            ) {
                PinchImageScreen()
            }
            Box(modifier = Modifier
                .constrainAs(box1) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(pinchView.top)
                    height= Dimension.fillToConstraints
                }
                .fillMaxWidth()
                .background(Color.Red)
            ) {
                Text("aaaaa")
            }
            Box(modifier = Modifier
                .constrainAs(box2) {
                    top.linkTo(box1.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(pinchView.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height= Dimension.fillToConstraints
                }
                .background(Color.Green)
            ) {
                Button(onClick = { }) {
                    Text(text = "Click")
                }
            }
        }
    }
}

/**
 * touch down x pointer move
 */
@Composable
fun TouchSlopScreen() {
    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    var width by remember { mutableStateOf(0f) }
    Box(
        Modifier
            .fillMaxSize()
            .onSizeChanged { width = it.width.toFloat() }
    ) {
        Box(
            Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .fillMaxHeight()
                .width(50.dp)
                .background(Color.Blue)
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            val down = awaitFirstDown()
                            val change =
                                awaitHorizontalTouchSlopOrCancellation(down.id) { change, over ->
                                    val originalX = offsetX.value
                                    val newValue =
                                        (originalX + over).coerceIn(0f, width - 50.dp.toPx())
                                    change.consume()
                                    offsetX.value = newValue
                                }
                            if (change != null) {
                                horizontalDrag(change.id) {
                                    val originalX = offsetX.value
                                    val newValue = (originalX + it.positionChange().x)
                                        .coerceIn(0f, width - 50.dp.toPx())
                                    it.consume()
                                    offsetX.value = newValue
                                }
                            }
                        }
                    }
                }
        )
    }
}

/**
 * touch move up and down
 */
@Composable
fun ScrollableScreen() {
    // actual composable state that we will show on UI and update in `Scrollable`
    val offset = remember { mutableStateOf(0f) }
    Box(
        Modifier
            .size(150.dp)
            .scrollable(
                orientation = Orientation.Vertical,
                // state for Scrollable, describes how consume scroll amount
                state = rememberScrollableState { delta ->
                    // use the scroll data and indicate how much this element consumed.
                    // unconsumed deltas will be propagated to nested scrollables (if present)
                    offset.value = offset.value + delta // update the state
                    delta // indicate that we consumed all the pixels available
                }
            )
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        // Modifier.scrollable is not opinionated about its children's layouts. It will however
        // promote nested scrolling capabilities if those children also use the modifier.
        // The modifier will not change any layouts so one must handle any desired changes through
        // the delta values in the scrollable state
        Text(offset.value.roundToInt().toString(), style = TextStyle(fontSize = 32.sp))
    }
}

@Composable
fun DoubleTapScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .clipToBounds()
            .background(Color.LightGray)
    ) {
        // set up all transformation states
        var scale by remember { mutableStateOf(1f) }
        var rotation by remember { mutableStateOf(0f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        val coroutineScope = rememberCoroutineScope()
        // let's create a modifier state to specify how to update our UI state defined above
        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            // note: scale goes by factor, not an absolute difference, so we need to multiply it
            scale *= zoomChange
            rotation += rotationChange
            offset += offsetChange
        }
        Box(
            Modifier
                // apply pan offset state as a layout transformation before other modifiers
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                // add transformable to listen to multitouch transformation events after offset
                .transformable(state = state)
                // optional for example: add double click to zoom
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            coroutineScope.launch { state.animateZoomBy(4f) }
                        }
                    )
                }
                .fillMaxSize()
                .border(1.dp, Color.Green),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "\uD83C\uDF55",
                fontSize = 32.sp,
                // apply other transformations like rotation and zoom on the pizza slice emoji
                modifier = Modifier.graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation
                )
            )
        }
    }

}

@Composable
fun PinchImageScreen(modifier : Modifier = Modifier) {
    val viewModel = viewModel<PinchZoomViewModel>(LocalContext.current as BaseComponentActivity)
    var offset by remember { mutableStateOf(viewModel.offset) }
    var zoom by remember { mutableStateOf(viewModel.zoom) }
    var angle by remember { mutableStateOf(viewModel.angle) }
    val coroutineScope = rememberCoroutineScope()
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        // note: scale goes by factor, not an absolute difference, so we need to multiply it
        zoom = zoomChange
        angle = rotationChange
        offset = offsetChange
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val screenWidth = with(LocalDensity.current) {
            this@BoxWithConstraints.maxWidth.roundToPx()
        }
        val screenHeight = with(LocalDensity.current) {
            this@BoxWithConstraints.maxHeight.roundToPx()
        }
        val scale = if (screenWidth > screenHeight) {
            ContentScale.FillHeight
        } else {
            ContentScale.FillWidth
        }
        val screenSize = Size(screenWidth, screenHeight)
        GlideImage(
            previewPlaceholder = R.drawable.ic_launcher_foreground,
            imageModel = "https://cdn.pixabay.com/photo/2020/07/14/16/18/snow-5404785_960_720.jpg",
            modifier = Modifier
                .transformable(state)
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { centroid, pan, gestureZoom, gestureRotate ->
                            val oldScale = zoom
                            val newScale = zoom * gestureZoom
                            // For natural zooming and rotating, the centroid of the gesture should
                            // be the fixed point where zooming and rotating occurs.
                            // We compute where the centroid was (in the pre-transformed coordinate
                            // space), and then compute where it will be after this delta.
                            // We then compute what the new offset should be to keep the centroid
                            // visually stationary for rotating and zooming, and also apply the pan.
                            val localOffset = (offset + centroid / oldScale).rotateBy(gestureRotate) -
                                    (centroid / newScale + pan / oldScale)
                            zoom = newScale
                            offset = localOffset
                            angle += gestureRotate
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = {
                        Log.e("LEE", "onDoubleTap()")
                        if (zoom == 1.0f) {
                            val targetScale = 2.0f
                            // TODO offset 잡아야 함.
                            val x = screenSize.width / targetScale
                            val y = screenSize.height / targetScale
                            val offsetRect = Rect(0f, 0f, x, y)

                            resetTransform(
                                scope = coroutineScope,
                                state = state,
                                zoom = targetScale,
                                offset = Offset(
                                    x = offsetRect.center.x,
                                    y = offsetRect.center.y
                                ),
                                useDelay = false
                            )
                        } else {
                            resetTransform(
                                scope = coroutineScope,
                                state = state,
                                zoom = 1.0f,
                                useDelay = false
                            )
                        }
                    })
                }
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            //Detect a touch down event
                            awaitFirstDown()
                            do {
                                val event : PointerEvent = awaitPointerEvent()
                                event.changes.forEach { pointerInputChange : PointerInputChange ->
                                    //Consume the change
                                }
                            } while (event.changes.any { it.pressed })

                            // Touch released - ACTION_UP
                            if (zoom < 1.0f) {
                                resetTransformAnimationJob?.cancel()
                                resetTransformAnimationJob = resetTransform(coroutineScope, state)
                            }
                            val checkOffset = checkMinMax(offset, viewModel.size.value, zoom, screenSize)
                            if (checkOffset != null) {
                                resetOffsetAnimationJob?.cancel()
                                resetOffsetAnimationJob = resetOffset(
                                    scope = coroutineScope, state = state, zoom = zoom, offset = checkOffset, angle = angle
                                )
                            }
                        }
                    }
                }
                .graphicsLayer {
                    translationX = -offset.x * zoom
                    translationY = -offset.y * zoom
                    scaleX = zoom
                    scaleY = zoom
//                    rotationZ = angle
                    transformOrigin = TransformOrigin(0f, 0f)
                }
                .background(Color.Blue)
                .fillMaxSize(),
            contentScale = scale,
            requestListener = object : RequestListener<Drawable> {
                override fun onResourceReady(resource : Drawable?, model : Any?, target : Target<Drawable>?, dataSource : DataSource?, isFirstResource : Boolean) : Boolean {
                    if (target?.request?.isComplete == true) {
                        resource?.let {
                            viewModel.setSize(Size(it.intrinsicWidth, it.intrinsicHeight))
                        }
                    }
                    return true
                }

                override fun onLoadFailed(e : GlideException?, model : Any?, target : Target<Drawable>?, isFirstResource : Boolean) : Boolean {
                    return true
                }

            }
        )
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            stopJob()
            viewModel.storeValue(
                angle = angle,
                offset = offset,
                zoom = zoom
            )
        }
    }
}

/**
 * drag시에 edge 점검을 위한 함수
 */
fun checkMinMax(offset : Offset, imageSize : Size, scale : Float, screenSize : Size) : Offset? {
    val x = offset.x
    val y = offset.y
    val minX = 0f
    val minY = 0f
    val maxX = screenSize.width - (screenSize.width / scale)
    val maxY = screenSize.height - (screenSize.height / scale)
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

    return checkOffset
}

/**
 * ref: https://developer.android.com/reference/kotlin/androidx/compose/foundation/gestures/package-summary?authuser=19#(androidx.compose.ui.Modifier).draggable(androidx.compose.foundation.gestures.DraggableState,androidx.compose.foundation.gestures.Orientation,kotlin.Boolean,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Boolean,kotlin.coroutines.SuspendFunction2,kotlin.coroutines.SuspendFunction2,kotlin.Boolean)
 */
fun Offset.rotateBy(angle : Float) : Offset {
    val angleInRadians = angle * PI / 180
    return Offset(
        (x * cos(angleInRadians) - y * sin(angleInRadians)).toFloat(),
        (x * sin(angleInRadians) + y * cos(angleInRadians)).toFloat()
    )
}

/**
 * TransformableState을 이용한 Transformation
 */
fun resetOffset(
    scope : CoroutineScope, state : TransformableState, zoom : Float = 1f, offset : Offset = Offset(0f, 0f), angle : Float = 0f
) : Job {
    return scope.launch {
        delay(100)
        state.transform() {
            this.transformBy(zoom, offset, angle)
        }
    }
}

/**
 * TransformableState을 이용한 Transformation
 */
fun resetTransform(
    scope : CoroutineScope, state : TransformableState, zoom : Float = 1f, offset : Offset = Offset(0f, 0f), angle : Float = 0f,
    useDelay : Boolean = true
) : Job {
    return scope.launch {
        if (useDelay) delay(500)
        state.transform() {
            this.transformBy(zoom, offset, angle)
        }
    }
}

/**
 * TODO 확장 로직 수정해야함.
 */
suspend fun TransformableState.animateZoomByInit(
    zoomFactor : Float,
    animationSpec : AnimationSpec<Float> = SpringSpec(stiffness = Spring.StiffnessLow)
) {
    require(zoomFactor > 0) {
        "zoom value should be greater than 0"
    }
    var previous = 1f
    transform {
        AnimationState(initialValue = zoomFactor).animateTo(previous, animationSpec) {
            val scaleFactor = if (previous == 0f) 1f else this.value / zoomFactor
            transformBy(zoomChange = scaleFactor)
            previous = this.value
        }
    }
}

@Preview(name = "PinchZoomScreen")
@Composable
fun PinchZoomScreenPreview() {
    PinchZoomScreen()
}