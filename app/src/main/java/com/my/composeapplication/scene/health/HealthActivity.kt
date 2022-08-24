package com.my.composeapplication.scene.health

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Pentagon
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.my.composeapplication.base.*
import com.my.composeapplication.scene.SimpleListActivity
import com.my.composeapplication.scene.bmi.CustomTopAppBar
import com.my.composeapplication.scene.health.data.TodoItem
import com.my.composeapplication.viewmodel.HealthViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by YourName on 2022/08/22.
 */
@AndroidEntryPoint
class HealthActivity : BaseComponentActivity() {
    private val viewModel by viewModels<HealthViewModel>()
    override fun getContent() : @Composable () -> Unit = {
        TodoListHoisting()
    }
}

class TodoListState(
    val viewModel : HealthViewModel,
    val allCheckState : MutableState<Boolean>,
    val scrollState : LazyListState
) {

}

@Composable
fun rememberTodoListState(
    viewModel : HealthViewModel = viewModel(
        LocalContext.current as BaseComponentActivity
    ),
    allCheckState : MutableState<Boolean> = rememberSaveable {
        mutableStateOf(false)
    },
    scrollState : LazyListState = rememberLazyListState()
) = remember(viewModel, allCheckState, scrollState) {
    TodoListState(
        viewModel = viewModel,
        allCheckState = allCheckState,
        scrollState = scrollState
    )
}

@Composable
private fun TodoListHoisting() {
    val todoListState = rememberTodoListState()
    var allCheckState by todoListState.allCheckState

    val onCheck : (TodoItem, Boolean) -> Unit = { item, isCheck ->
        todoListState.viewModel.setCheck(item, isCheck)
    }
    val onRemove : (TodoItem) -> Unit = { item ->
        todoListState.viewModel.removeItem(item)
    }

    val onCheckAll : () -> Unit = {
        val nextState = !allCheckState
        todoListState.viewModel.allCheck(nextState)
        allCheckState = nextState
    }

    val context = (LocalContext.current as BaseComponentActivity).baseContext
    val goActivity : () -> Unit = {
        context.startActivity(Intent(context, SimpleListActivity::class.java))
    }

    // TODO 해당 키가 변경될때만 하위 Scope를 실행함. Recomposition되어도 실행되지 않음, 컴포지션이 삭제되면 해당 Scope는 취소됨.
    LaunchedEffect(key1 = true) {
        Log.e(HealthActivity::class.simpleName, "TodoListHoisting() LaunchedEffect")
    }

    customScaffold(
        topAppbar = {
            CustomTopAppBar(
                title = "Checkable List",
                actions = {
                    IconButton(onClick = onCheckAll) {
                        Icon(
                            imageVector = if (allCheckState)
                                Icons.Default.Undo
                            else
                                Icons.Default.DoneAll,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = goActivity) {
                        Icon(
                            imageVector = Icons.Default.Pentagon,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        TodoList(
            todoListState,
            onCheck,
            onRemove,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun TodoList(
    todoListState : TodoListState,
    onChecked : (item : TodoItem, state : Boolean) -> Unit,
    onClose : (item : TodoItem) -> Unit,
    modifier : Modifier = Modifier
) {
    val list = todoListState.viewModel.list
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = todoListState.scrollState
    ) {
        items(list) { item ->
            TodoItemView(
                item = item,
                onChecked = { isChecked ->
                    onChecked(item, isChecked)
                },
                onClose = onClose
            )
        }
        if (list.isNotEmpty()) {
            item {
                BottomProgress()
            }
        }
    }
    todoListState.scrollState.OnEndItem {
        todoListState.viewModel.moreList()
    }
}

@Composable
fun BottomProgress(modifier : Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(30.dp),
            color = Color.Black
        )
    }
}

@Composable
fun TodoItemView(
    item : TodoItem,
    onChecked : (state : Boolean) -> Unit,
    onClose : (item : TodoItem) -> Unit,
    modifier : Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.Black))
            .padding(
                horizontal = 10.dp, vertical = 5.dp
            )
    ) {
        Text(
            text = item.text,
            modifier = Modifier
                .height(30.dp)
                .weight(1f),
            maxLines = 1,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(10.dp))
        Checkbox(
            checked = item.isChecked, onCheckedChange = onChecked,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            onClick = { onClose(item) },
            modifier = Modifier.size(30.dp)
        ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = null)
        }
    }
}

@Preview(name = "BottomProgress")
@Composable
fun BottomProgressPreview() {
    BottomProgress()
}

@Preview(name = "TodoItemView")
@Composable
fun TodoItemViewPreview() {
    TodoItemView(TodoItem(1, "xxxx"), {}, {})
}