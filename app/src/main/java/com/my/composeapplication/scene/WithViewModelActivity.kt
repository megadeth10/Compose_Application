package com.my.composeapplication.scene

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.my.composeapplication.base.BaseComponentActivity
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState

/**
 * Created by YourName on 2022/08/16.
 * ViewModel 샘플 코드
 */
class WithViewModelActivity : BaseComponentActivity() {
    override fun getContent() : @Composable () -> Unit = {
        FirstScreen()
    }
}

@Composable
fun FirstScreen() {
    val (text1, setText) = rememberSaveable {
        mutableStateOf("hello")
    }
    val text2 by rememberSaveable {
        mutableStateOf("hello")
    }
    val viewModel = viewModel<WithViewModel>()
    val liveData = viewModel.liveData.observeAsState("")
    Surface {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = viewModel.data.value ?: "")
            Button(onClick = { viewModel.setData("World") }) {
                Text(text = "변경")
            }
        }
    }
}

class WithViewModel: ViewModel() {
    private var _liveData: MutableLiveData<String> = MutableLiveData("")
    val liveData: LiveData<String>  get() = _liveData
    fun setLiveData(newState: String) {
        this._liveData.value = newState
    }

    private val _data = mutableStateOf<String>("")
    val data: State<String> get() = _data
    fun setData(newState: String) {
        this._data.value = newState
    }
    init {
        Log.e("WithViewMode", "init() this: $this")
    }
}