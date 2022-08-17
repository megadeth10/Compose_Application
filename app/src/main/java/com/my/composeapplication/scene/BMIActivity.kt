package com.my.composeapplication.scene

import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.my.composeapplication.viewmodel.BMIViewModel
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.CustomScaffold
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by YourName on 2022/08/16.
 */

private var snackbarHostState : SnackbarHostState? = null

@AndroidEntryPoint
class BMIActivity : BaseComponentActivity() {
    override fun getContent() : @Composable () -> Unit = {
        val viewModel: BMIViewModel by viewModels()
        MainScreenHoisting(viewModel)
    }
}

@Composable
private fun MainScreenHoisting(viewModel : BMIViewModel = viewModel()) {
    val heightState = viewModel.height.observeAsState()
    val weightState = viewModel.weight.observeAsState()
    MainScreen(
        height = heightState.value ?: "",
        weight = weightState.value ?: "",
        setHeight = viewModel::setHeight,
        setWeight = viewModel::setWeight
    )
}

@Composable
fun MainScreen(
    height : String,
    weight : String,
    setHeight : (String) -> Unit,
    setWeight : (String) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        snackbarHostState = CustomScaffold(
            topAppbar = {
                CustomTopAppBar("BIM Calculator")
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                TextField(text = "BMI Test")
                InputField(
                    height,
                    setHeight,
                    "Weight", "input weight",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))
                InputField(
                    weight,
                    setWeight,
                    "Height", "input height",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(title : String) {
    SmallTopAppBar(
        title = {
            Text(title)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value : String,
    setState : (String) -> Unit,
    label : String, placeHolder : String, modifier : Modifier = Modifier,
    keyboardOptions : KeyboardOptions = KeyboardOptions().copy(
        keyboardType = KeyboardType.Number
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = setState,
        modifier = modifier,
        label = { TextField(label, Color.Gray) },
        placeholder = { TextField(placeHolder, Color.Gray) },
        keyboardOptions = keyboardOptions,
        singleLine = true,
    )
}

@Composable
fun TextField(text : String, textColor : Color = Color.Black) {
    Text(text = text, color = textColor)
}

@Preview
@Composable
fun MainPreview() {
    MainScreen("0", "0", {}, {})
}