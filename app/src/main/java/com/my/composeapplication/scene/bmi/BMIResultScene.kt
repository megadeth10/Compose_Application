package com.my.composeapplication.scene.bmi

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.my.composeapplication.R
import com.my.composeapplication.base.customScaffold
import com.my.composeapplication.enum.BMIResult
import com.my.composeapplication.viewmodel.BMIViewModel

/**
 * Created by YourName on 2022/08/18.
 */
@Composable
fun ResultScreenHoisting(navController : NavHostController?, level : Int, viewModel : BMIViewModel = viewModel(LocalContext.current as ComponentActivity)) {
    ResultScreen(level = level) {
        viewModel.setHeight("")
        viewModel.setWeight("")
        navController?.popBackStack()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(level : Int, onBack : () -> Unit) {
    customScaffold(
        topAppbar = {
            CustomTopAppBar(
                title = stringResource(id = R.string.result),
                showBack = true
            ) {
                onBack()
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = it.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(
                        id =
                        BMIResult.getResultImageId(level)
                    ),
                    contentDescription = "BMI Result Image",
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                )
                Text(
                    text = stringResource(
                        id = BMIResult.getResultStringId(level)
                    ),
                    style = TextStyle.Default.copy(
                        fontSize = 18.sp
                    )
                )
            }
        }

    }
}

@Preview
@Composable
fun ResultScreenPreview() {
//    ResultScreen(1, {})
    ResultScreenHoisting(null, 1, BMIViewModel())
}