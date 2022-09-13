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
import com.my.composeapplication.base.CheckBoxGroupCompose
import com.my.composeapplication.base.RadioGroupCompose
import com.my.composeapplication.base.data.CheckGroupState
import com.my.composeapplication.base.data.RadioGroupState
import com.my.composeapplication.viewmodel.GroupButtonViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by YourName on 2022/09/08.
 */
@AndroidEntryPoint
class GroupButtonActivity : BaseComponentActivity() {
    private val viewModel : GroupButtonViewModel by viewModels()
    override fun getContent() : @Composable () -> Unit = {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButtonScreen()
            CheckBoxGroupScreen()
        }
    }
}

@Composable
fun RadioButtonScreen() {
    val viewModel : GroupButtonViewModel = viewModel(LocalContext.current as BaseComponentActivity)
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    RadioGroupCompose(
        modifier = Modifier.width(200.dp),
        radioGroupState = RadioGroupState(
            menuList = list.toList(),
            selected = viewModel.selected,
            isMulti = true
        ),
        onChangeSelected = {
            viewModel.setRadioSelected(it)
        }
    )
}

@Composable
fun CheckBoxGroupScreen() {
    val viewModel: GroupButtonViewModel = viewModel(LocalContext.current as BaseComponentActivity)
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    CheckBoxGroupCompose(
        checkGroupState = CheckGroupState(
            itemList = list.toList(),
            checkedItems = viewModel.checkBoxSelected
        ),
        onCheckedChange = {
            viewModel.setCheckSelected(it)
        }
    )
}