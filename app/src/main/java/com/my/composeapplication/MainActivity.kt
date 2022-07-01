package com.my.composeapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import com.my.composeapplication.databinding.ActivityMainBinding
import com.my.composeapplication.ui.theme.ComposeApplicationTheme
import com.my.composeapplication.ui.theme.White
import com.my.composeapplication.viewmodel.ConversationViewModel
import com.my.composeapplication.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private var _bind : ActivityMainBinding? = null
    private val bind get() = _bind!!

    override fun onCreate(savedInstanceState : Bundle?) {
        Log.e(MainActivity::class.simpleName, "onCreate()")
        super.onCreate(savedInstanceState)
        _bind = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(bind.root)

        bind.composeMain.setContent {
            ComposeApplicationTheme {
                // A surface container using the 'background' color from the theme
                MyApp(mainViewModel)
            }
        }
        val fragment = MainFragment()
        val tagName = MainFragment::class.simpleName
        supportFragmentManager.beginTransaction()
            .add(bind.container.id, fragment, tagName)
            .addToBackStack(tagName)
            .commit()
        this.mainViewModel.data.observe(this, Observer {
            Log.e(MainActivity::class.simpleName, "MainActivity data: $it")
        })
    }

    override fun onBackPressed() {
        finish()
//        super.onBackPressed()
    }
}

@Composable
private fun MyApp(mainViewModel : MainViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), color = MaterialTheme.colorScheme.background
    ) { //MaterialTheme.colorScheme.background) {
        Column() {
            Row() {
                Greeting("안드로이드", mainViewModel)
            }
            Row() {
                Greeting("Android", mainViewModel)
            }
        }
    }
}

@Composable
fun Greeting(name : String, mainViewModel: MainViewModel) {
    Log.e(MainActivity::class.simpleName, "Greeting() viewmodel: $mainViewModel")
    Text(
        text = "Hello $name!", style = TextStyle(color = White), modifier = Modifier
            .fillMaxWidth(1f)
            .background(Color.Transparent)
            .clickable(enabled = true, onClickLabel = "click", role = Role.Button, onClick = {
            }), textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, name = "preview")
@Composable
fun DefaultPreview(viewModel : MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    ComposeApplicationTheme {
        MyApp(viewModel)
    }
}