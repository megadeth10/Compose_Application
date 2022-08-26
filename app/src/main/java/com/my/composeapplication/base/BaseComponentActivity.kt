package com.my.composeapplication.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView

/**
 * Created by YourName on 2022/07/01.
 */
abstract class BaseComponentActivity : ComponentActivity() {
    abstract fun getContent() : @Composable () -> Unit
    protected var backPressCallback : (() -> Unit)? = null
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPressCallback?.invoke() ?: finish()
        }
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        setContentView(ComposeView(this).apply {
            this.setContent(getContent())
        })
    }

    override fun onDestroy() {
        this.onBackPressedCallback.remove()
        super.onDestroy()
    }
}
