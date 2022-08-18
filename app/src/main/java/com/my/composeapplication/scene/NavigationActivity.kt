package com.my.composeapplication.scene

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.customScaffold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by YourName on 2022/08/16.
 */
private var snackbarHostState : SnackbarHostState? = null

class NavigationActivity : BaseComponentActivity() {
    override fun getContent() : @Composable () -> Unit = {
        NavScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbarHostState = null
    }
}

@Composable
fun NavScreen() {
    val navController = rememberNavController()
    snackbarHostState = customScaffold {
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun FirstScreen(navController : NavHostController) {
    val (text, setText) = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
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
                    snackbarHostState?.let {
                        it.currentSnackbarData?.dismiss()
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        scope.launch(Dispatchers.Main) {
                            it.showSnackbar("입력이 필요합니다.", "닫기")
                        }
                    }
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
