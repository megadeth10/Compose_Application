package com.my.composeapplication.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView

/**
 * Created by YourName on 2022/07/01.
 */
abstract class BaseComponentActivity : ComponentActivity() {
    abstract fun getContent() : @Composable () -> Unit
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ComposeView(this).apply {
            this.setContent(getContent())
        })
    }
}
