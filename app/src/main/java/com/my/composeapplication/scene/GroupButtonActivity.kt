package com.my.composeapplication.scene

import androidx.activity.viewModels
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.my.composeapplication.R
import com.my.composeapplication.base.*
import com.my.composeapplication.base.data.CheckGroupState
import com.my.composeapplication.base.data.RadioGroupState
import com.my.composeapplication.base.data.SwitchGroupState
import com.my.composeapplication.viewmodel.GroupButtonViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by YourName on 2022/09/08.
 * Radio, Check, Switch로 Group Compose 셈플
 */
@AndroidEntryPoint
class GroupButtonActivity : BaseComponentActivity() {
    private val viewModel : GroupButtonViewModel by viewModels()
    override fun getContent() : @Composable () -> Unit = {
        val scrollableState = rememberScrollState()
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(scrollableState),
        ) {
            RadioButtonScreen()
            CheckBoxGroupScreen()
            CheckBoxAllGroupScreen()
            SwitchGroupScreen()
        }
    }
}

@Composable
fun RadioButtonScreen() {
    val viewModel : GroupButtonViewModel = viewModel(LocalContext.current as BaseComponentActivity)
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    RadioGroupHoisting(
        modifier = Modifier.width(200.dp),
        radioGroupState = RadioGroupState(
            itemList = list.toList(),
            checkedItems = viewModel.selected,
            isMulti = true
        ),
        onChangeSelected = {
            viewModel.setRadioSelected(it)
        },
    )
}

@Composable
fun CheckBoxGroupScreen() {
    val viewModel: GroupButtonViewModel = viewModel(LocalContext.current as BaseComponentActivity)
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    CheckboxGroupHoisting(
        modifier = Modifier.width(200.dp),
        checkGroupState = CheckGroupState(
            itemList = list.toList(),
            checkedItems = viewModel.checkBoxSelected
        ),
        onCheckedChange = {
            viewModel.setCheckSelected(it)
        },
    )
}

@Composable
fun CheckBoxAllGroupScreen() {
    val viewModel: GroupButtonViewModel = viewModel(LocalContext.current as BaseComponentActivity)
    val list = LocalContext.current.resources.getStringArray(R.array.option)
    CheckboxGroupWithAllHoisting(
        modifier = Modifier.fillMaxWidth(),
        groupTitle = "adlkjalkdjf;af",
        checkGroupState = CheckGroupState(
            itemList = list.toList(),
            checkedItems = viewModel.checkBoxSelected
        ),
        onCheckedChange = {
            viewModel.setCheckSelected(it)
        },
        isCheckAll = viewModel.checkBoxAllSelected,
        onCheckedAllChange = {
            viewModel.setCheckAll(it)
        },
    )
}

@Composable
fun SwitchGroupScreen() {
    val viewModel: GroupButtonViewModel = viewModel(LocalContext.current as BaseComponentActivity)
    val list = LocalContext.current.resources.getStringArray(R.array.option)

    SwitchGroupHoisting(
        modifier = Modifier.width(200.dp),
        switchGroupState = SwitchGroupState(
            itemList = list.toList(),
            checkedItems = viewModel.checkBoxSelected
        ),
        onCheckChange = {
            viewModel.setCheckSelected(it)
        },
        reversLayout = false
    )
}