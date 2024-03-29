package com.my.composeapplication.scene

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.CustomScaffold
import com.my.composeapplication.base.SnackbarWrappingCompose
import com.my.composeapplication.viewmodel.NavigationViewModel

/**
 * Created by YourName on 2022/08/16.
 * Compose의 Navigater 샘플 코드
 */
class NavigationActivity : BaseComponentActivity() {
    private val viewModel : NavigationViewModel by viewModels()
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setInputMode(window.attributes.softInputMode)
    }

    override fun getContent() : @Composable () -> Unit = {
        NavScreen()
    }

}

@Composable
fun NavScreen(
    viewModel : NavigationViewModel = viewModel(LocalContext.current as BaseComponentActivity)
) {
    val navController = rememberNavController()

    CustomScaffold {
        SnackbarWrappingCompose(
            modifier = Modifier.fillMaxSize(),
            snackbarHostState = viewModel.snackbarState.value,
            isAdjustResizeMode = viewModel.isAdjustInputMode()
        ) {
            NavHost(navController = navController, startDestination = "first") {
                composable("first") {
                    FirstScreen(navController)
                }
                composable("second") {
                    SecondScreen(navController)
                }
                composable("third/{value}") { backStackEntry ->
                    ThirdScreen(navController, backStackEntry.arguments?.getString("value") ?: "")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun FirstScreen(
    navController : NavHostController,
    viewModel : NavigationViewModel = viewModel(LocalContext.current as BaseComponentActivity)
) {
    val (text, setText) = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Surface {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "첫 화면")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.navigate("second")
            }) {
                Text(text = "두번째 이동")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = text, onValueChange = setText)
            Button(onClick = {
                if (text.isNotEmpty()) {
                    navController.navigate("third/$text")
                } else {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    viewModel.showSnackbar(
                        message = "입력이 필요합니다.",
                        actionLabel = "닫기"
                    )
                }
            }) {
                Text(text = "세번째 이동")
            }
        }
    }
}

@Composable
fun SecondScreen(navController : NavHostController) {
    Surface {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "두번째 화면")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.popBackStack()
            }) {
                Text(text = "뒤로 가기")
            }
        }
    }
}

@Composable
fun ThirdScreen(navController : NavHostController, text : String) {
    Surface {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "새번째 화면")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = text)
            Button(onClick = {
                navController.popBackStack()
            }) {
                Text(text = "뒤로 가기")
            }
        }
    }
}
