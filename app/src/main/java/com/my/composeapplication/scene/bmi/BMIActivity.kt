package com.my.composeapplication.scene.bmi

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.my.composeapplication.viewmodel.BMIViewModel
import com.my.composeapplication.ui.theme.PurpleGrey80
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by YourName on 2022/08/16.
 */

private var snackbarHostState : SnackbarHostState? = null

@AndroidEntryPoint
class BMIActivity : BaseComponentActivity() {
    private val viewModel by viewModels<BMIViewModel>()
    override fun getContent() : @Composable () -> Unit = {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "input") {
            composable("input") {
                MainScreenHoisting(navController)
                CloseToastHoisting(snackbarHostState)
            }
            composable("result/{level}") {
                val level = it.arguments?.getString("level")?.toInt() ?: 0
                ResultScreenHoisting(navController, level)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MainScreenHoisting(
    navController : NavHostController,
    viewModel : BMIViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
//    val heightState = viewModel.height.observeAsState()
//    val weightState = viewModel.weight.observeAsState()
    val heightState = viewModel.height.value.trim()
    val weightState = viewModel.weight.value.trim()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    val bmiResult : () -> Unit = {
        focusManager.clearFocus()
        keyboardController?.hide()
        if (validation(heightState, weightState)) {
            val level = (heightState.toInt().div(weightState.toInt()))
            navController.navigate("result/$level")
        } else {
            snackbarHostState?.let {
                it.currentSnackbarData?.dismiss()
                coroutineScope.launch {
                    it.showSnackbar("입력값을 확인해 주세요", "닫기")
                }
            }
        }
    }
    MainScreen(
        height = heightState,
        weight = weightState,
        bmiResult
    )
}


@Composable
fun MainScreen(
    height : String,
    weight : String,
    onResult : () -> Unit = {},
) {
    val viewModel : BMIViewModel = viewModel(LocalContext.current as ComponentActivity)
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
    snackbarHostState = customScaffold(
        topAppbar = {
            CustomTopAppBar("BMI Calculator")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = it.calculateTopPadding())
        ) {
            TextField(text = "BMI Test")
            weightHighLightRef = highlightView() {
                InputField(
                    weight,
                    viewModel::setWeight,
                    "Weight", "input weight",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions().copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            heightHighLightRef = highlightView() {
                InputField(
                    height,
                    viewModel::setHeight,
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
fun CustomTopAppBar(title : String, showBack : Boolean = false, onBack : (() -> Unit)? = null) {
    val scrollState = rememberTopAppBarState()
    SmallTopAppBar(
        title = {
            Text(title)
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = PurpleGrey80),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(scrollState),
        navigationIcon = {
            if (showBack) {
                Box(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                onBack?.invoke()
                            }
                            .width(30.dp)
                            .height(30.dp)
                    )
                }
            }
        }
    )
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

@Preview
@Composable
fun MainPreview() {
    MainScreen("", "", {})
}