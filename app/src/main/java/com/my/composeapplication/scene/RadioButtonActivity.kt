package com.my.composeapplication.scene

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.my.composeapplication.R
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.base.RadioGroupView
import com.my.composeapplication.base.data.RadioGroupState
import com.my.composeapplication.viewmodel.RadioButtonViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by YourName on 2022/09/08.
 */
@AndroidEntryPoint
class RadioButtonActivity : BaseComponentActivity() {
    private val viewModel : RadioButtonViewModel by viewModels()
    override fun getContent() : @Composable () -> Unit = {
        RadioButtonScreen()
    }
}

@Composable
fun RadioButtonScreen() {
    val viewModel : RadioButtonViewModel = viewModel(LocalContext.current as BaseComponentActivity)
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    RadioGroupView(
        modifier = Modifier.width(200.dp),
        radioGroupState = RadioGroupState(
            menuList = list.toList(),
            selected = viewModel.selected,
            isMulti = true
        ),
        onChangeSelected = {
            viewModel.setSelected(it)
        }
    )
}