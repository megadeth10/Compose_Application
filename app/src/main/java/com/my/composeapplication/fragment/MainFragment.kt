package com.my.composeapplication.fragment

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.my.composeapplication.R
import com.my.composeapplication.databinding.FragmentMainBinding
import com.my.composeapplication.databinding.LayoutInputBinding
import com.my.composeapplication.scene.Greeting
import com.my.composeapplication.ui.theme.ComposeApplicationTheme
import com.my.composeapplication.ui.theme.Gray
import com.my.composeapplication.viewmodel.MainViewModel


/**
 * Created by YourName on 2022/06/27.
 */
class MainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding
    private val viewModel : MainViewModel by activityViewModels()
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        Log.e(MainFragment::class.simpleName, "onCreateView()")
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        var view : View? = View(requireContext())
        binding?.let {
            view = it.root
            it.composeView.setContent {
                main(viewModel)
            }
            it.composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            it.containerMiddle.addView(ComposeView(requireContext()).apply {
                this.id = R.id.composeView1 //리소스로 생성한 아이디로 설정함.
                this.setContent {
                    body(viewModel)
                }
            })
        }
        return view
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

@Composable
fun main(viewModel : MainViewModel) {
    Column() {
        Row() {
            content(viewModel)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row (){
            HelloScreen()
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row() {
            Button(onClick = {
                val state = viewModel.data.value ?: false
                viewModel.setData(!state)
            },
                content = { Text("변경") }
            )
        }
    }
}

@Composable
fun body(mainViewModel : MainViewModel) {
    Greeting(name = "subView", mainViewModel)
}

@Composable
fun inputLayout(viewModel : MainViewModel) {
    ComposeApplicationTheme {
        // AndroidViewBinding API를 통한 view binding
        val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
        val grayColor = Gray.toArgb()
        AndroidViewBinding(LayoutInputBinding::inflate) {
            etId.setTextColor(primaryColor)
            etId.setHintTextColor(grayColor)
            etPw.setTextColor(primaryColor)
            etPw.setHintTextColor(grayColor)
            this.root.rootView.findViewTreeLifecycleOwner()?.let {
                viewModel.data.observe(it, Observer {
                    Log.e(this.toString(), "inputLayout data: $it")
                })
            }
        }
    }
}

@Composable
fun HelloScreen() {
    val name = remember { mutableStateOf("") }
    HelloContent(name = name.value, onNameChange = { name.value = it })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelloContent(name : String, onNameChange : (String) -> Unit) {
    Column(modifier = Modifier
        .padding(16.dp)
        .background(MaterialTheme.colorScheme.surface) ) {
        Row {
            Column {
                Text(
                    text = name,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                )
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = name, onValueChange = onNameChange, label = { Text("Name") })
            }
        }
        Row(modifier = Modifier.padding(all = 1.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "topText: $name", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 5.dp,) {
                    Text(text = "bottomText", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(start = 5.dp, end = 5.dp))
                }
            }
        }
    }
}

@Composable
//fun content(viewModel : MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
fun content(viewModel : MainViewModel) {
    ComposeApplicationTheme {
        Surface() {
            Column {
                Row {
                    Greeting(name = "Compose", viewModel)
                }
                Row {
                    inputLayout(viewModel)
                }
            }
        }
    }
}

@Preview(name = "Light Mode", uiMode = UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun defaultLightPreview(viewModel : MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    main(viewModel)
}


@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun defaultDarkPreview(viewModel : MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    main(viewModel)
}


