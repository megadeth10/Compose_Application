package com.my.composeapplication.scene.bmi

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.my.composeapplication.base.*
import com.my.composeapplication.ui.theme.PurpleGrey80
import com.my.composeapplication.viewmodel.BMIViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by YourName on 2022/08/16.
 */

private var snackbarHostState : MutableState<SnackbarHostState>? = null

@AndroidEntryPoint
class BMIActivity : BaseComponentActivity() {
    private val viewModel by viewModels<BMIViewModel>()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun getContent() : @Composable () -> Unit = {
        val appState = rememberMainScreenState()
        NavHost(navController = appState.navController, startDestination = "input") {
            composable("input") {
                MainScreenHoisting(appState)
                CloseToastHoisting(snackbarHostState?.value)
            }
            composable("result/{level}") {
                val level = it.arguments?.getString("level")?.toInt() ?: 0
                ResultScreenHoisting(appState.navController, level)
            }
        }
    }
}

class MainScreenState @OptIn(ExperimentalComposeUiApi::class) constructor(
    val viewModel : BMIViewModel,
    val focusManager : FocusManager,
    val keyboardController : SoftwareKeyboardController?,
    val navController : NavHostController,
) {
    fun goResult(level : Int) {
        navController.navigate("result/$level")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberMainScreenState(
    viewModel : BMIViewModel = viewModel(LocalContext.current as BaseComponentActivity),
    focusManager : FocusManager = LocalFocusManager.current,
    keyboardController : SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    navController : NavHostController = rememberNavController()
) = remember(viewModel, focusManager, keyboardController, navController) {
    MainScreenState(
        viewModel = viewModel,
        focusManager = focusManager,
        keyboardController = keyboardController,
        navController = navController
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MainScreenHoisting(appState : MainScreenState) {
    val heightState = appState.viewModel.height.value.trim()
    val weightState = appState.viewModel.weight.value.trim()

    val bmiResult : () -> Unit = {
        appState.focusManager.clearFocus()
        appState.keyboardController?.hide()
        if (validation(heightState, weightState)) {
            val level = (heightState.toInt().div(weightState.toInt()))
            appState.goResult(level)
        } else {
            showSnackbar(
                snackbarHostState = snackbarHostState?.value,
                message = "입력값을 확인해 주세요",
                actionLabel = "닫기"
            )
        }
    }
    MainScreen(
        height = heightState,
        weight = weightState,
        setWeight = { newValue -> appState.viewModel.setWeight(newValue) },
        setHeight = { newValue -> appState.viewModel.setHeight(newValue) },
        bmiResult
    )
}

@Composable
fun MainScreen(
    height : String,
    weight : String,
    setWeight : (String) -> Unit = {},
    setHeight : (String) -> Unit = {},
    onResult : () -> Unit = {},
) {
    var weightHighLightRef : ((Boolean) -> Unit)? = null
    var heightHighLightRef : ((Boolean) -> Unit)? = null

    val highLightResult : () -> Unit = {
        Log.e("BMIActivity", "MainScreen() height: $height weight: $weight")
        if (weight.isEmpty()) {
            weightHighLightRef?.invoke(true)
        }
        if (height.isEmpty()) {
            heightHighLightRef?.invoke(true)
        }
        onResult()
    }
    snackbarHostState = remember {
        mutableStateOf(SnackbarHostState())
    }
    CustomScaffold(
        snackbarHostState = snackbarHostState!!,
        topAppbar = {
            CustomTopAppBar(title = "BMI Calculator")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = it.calculateTopPadding())
        ) {
            TextField(text = "BMI Test")
            weightHighLightRef = highlightView {
                InputField(
                    weight,
                    setWeight,
                    "Weight", "input weight",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions().copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            heightHighLightRef = highlightView {
                InputField(
                    height,
                    setHeight,
                    "Height", "input height",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions().copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        highLightResult()
                    })
                )
            }
            CalculateButton(highLightResult)
        }
    }
}

@Composable
private fun CalculateButton(onResult : () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = {
                onResult()
            },
        ) {
            Text("계산")
        }
    }
}

private fun validation(height : String, weight : String) : Boolean {
    if (height.isNotEmpty() && weight.isNotEmpty()) {
        return true
    }
    return false
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value : String,
    setState : (String) -> Unit,
    label : String, placeHolder : String, modifier : Modifier = Modifier,
    keyboardActions : KeyboardActions = KeyboardActions(),
    keyboardOptions : KeyboardOptions = KeyboardOptions().copy(
        keyboardType = KeyboardType.Number,
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = setState,
        modifier = modifier,
        label = { TextField(label, Color.Black) },
        placeholder = { TextField(placeHolder, Color.Gray) },
        keyboardOptions = keyboardOptions,
        singleLine = true,
        keyboardActions = keyboardActions
    )
}

@Composable
fun TextField(text : String, textColor : Color = Color.Black) {
    Text(text = text, color = textColor)
}

@Preview(name = "CanvasPreview")
@Composable
fun CanvasPreview() {
    // TODO Canvas Draw
    Canvas(Modifier.size(120.dp)) {
        // Draw grey background, drawRect function is provided by the receiver
        drawRect(color = Color.Gray)

        // Inset content by 10 pixels on the left/right sides
        // and 12 by the top/bottom
        inset(10.0f, 12.0f) {
            val quadrantSize = size / 2.0f

            // Draw a rectangle within the inset bounds
            drawRect(
                size = quadrantSize,
                color = Color.Red
            )

            rotate(45.0f) {
                drawRect(size = quadrantSize, color = Color.Blue)
            }
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    MainScreen("", "") {

    }
}
