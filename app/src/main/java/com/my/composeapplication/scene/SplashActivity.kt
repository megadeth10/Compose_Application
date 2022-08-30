package com.my.composeapplication.scene

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.CustomScaffold
import com.my.composeapplication.scene.bmi.CustomTopAppBar
import com.my.composeapplication.scene.health.HealthActivity

/**
 * Created by YourName on 2022/08/26.
 */
class SplashActivity: BaseComponentActivity() {
    override fun getContent() : @Composable () -> Unit = {
        SplashScreen()
        this.backPressCallback = {
            Log.e("LEE","SplashActivity()")
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.e("LEE","onDestroy() SplashActivity")
    }
}

@Composable
fun SplashScreen() {
    val context = (LocalContext.current as BaseComponentActivity).baseContext
    val goActivity : () -> Unit = {
        context.startActivity(Intent(context, HealthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        })
    }
    CustomScaffold(
        topAppbar = {
            CustomTopAppBar(title = "SplashActivity")
        }
    ) {
        // Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT 재사용및 현재 위치로 activity 대치함.
        // activity singleTask 이면 재사용 하지만 task clear 된다.
        Button(
            modifier = Modifier.padding(it),
            onClick = { goActivity() }
        ) {
            Text(text = "Click reuse and replace")
        }
    }
}