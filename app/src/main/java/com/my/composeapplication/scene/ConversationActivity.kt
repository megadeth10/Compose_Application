package com.my.composeapplication.scene

/** by 설정에 아래 2가지 import가 필요하다. */
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.composeapplication.R
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.data.Message
import com.my.composeapplication.data.SampleData
import com.my.composeapplication.ui.theme.ComposeApplicationTheme
import com.my.composeapplication.ui.theme.Red

/**
 * Created by YourName on 2022/06/29.
 */
class ConversationActivity : BaseComponentActivity() {
    override fun getContent() : @Composable () -> Unit = {
                ListWithBug(myList = listOf(
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    "asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas","asdas",
                    ))
    }
}

@Composable
@Deprecated("Example with bug")
fun ListWithBug(myList : List<String>) {
    var items = 0
    var count by remember {
        mutableStateOf(1)
    }
    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.weight(0.5f)) {
            LazyColumn() {
                items(myList) { item ->
                    Text("Item: $item")
                    items++ // Avoid! Side-effect of the column recomposing.
                    Log.e("ConversationActivity", "LazyColumn $items")
                }
                Log.e("ConversationActivity", "LazyColumn end")
            }
            Text("Count: $items")
            Log.e("ConversationActivity", "Text end")
        }
        Row(modifier = Modifier.weight(0.5f)) {
            Button(onClick = { count = count.plus(1) }) {
                Text(text = "$count clicked items:${myList.size}")
            }
        }
    }
}

@Composable
fun body() {
    ComposeApplicationTheme {
        val context = LocalContext.current
        Column {
            Row {
                Button(onClick = {
                    context.startActivity(Intent(context, MainActivity::class.java))
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ExpandLess,
                            contentDescription = "button"
                        )
                        Text(text = "next Activity", )
                    }
                }
            }
            Row(modifier = Modifier.weight(0.5f)) {
                NameContent(SampleData.nameSample)
            }
            Row(modifier = Modifier.weight(0.5f)) {
                ListContainer()
            }
        }
    }
}

@Composable
fun NameContent(list : List<String>) {
    var name by remember {
        mutableStateOf("no name")
    }
    Log.e(ConversationActivity::class.simpleName, "NameContent()")
    NamePicker(header = name, names = list, onNameClicked = {
        Log.e(ConversationActivity::class.simpleName, "name click($it)")
        name = it
    })
}

/**
 * Display a list of names the user can click with a header
 */
@Composable
fun NamePicker(
    header : String,
    names : List<String>,
    onNameClicked : (String) -> Unit
) {
    Log.e(ConversationActivity::class.simpleName, "NamePicker()")
    Column {
        val color by animateFloatAsState(
            targetValue = 1.0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        // this will recompose when [header] changes, but not when [names] changes
        Text(
            header, style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.alpha(color),
            color = Red
        )
        Divider()

        // LazyColumn is the Compose version of a RecyclerView.
        // The lambda passed to items() is similar to a RecyclerView.ViewHolder.
        LazyColumn {
            items(names) { name ->
                // When an item's [name] updates, the adapter for that item
                // will recompose. This will not recompose when [header] changes
                NamePickerItem(name = name, onClicked = onNameClicked)
            }
        }
    }
}

/**
 * Display a single name the user can click.
 */
@Composable
fun NamePickerItem(name : String, modifier : Modifier = Modifier, onClicked : (String) -> Unit) {
    Log.e(ConversationActivity::class.simpleName, "NamePickerItem()")
    Row(
        Modifier
            .border(1.dp, MaterialTheme.colorScheme.secondary, CircleShape)
    ) {
        Text(
            name,
            modifier
                .clickable(onClick = { onClicked(name) })
                .fillMaxWidth(1.0f)
                .padding(5.dp)

        )
    }
}

@Composable
fun ListContainer() {
    Column() {
        Text(text = "start")
        buttonContainer()
        Conversation(message = SampleData.conversationSample)
    }
}

@Composable
fun buttonContainer() {
    var number by remember {
        mutableStateOf(1)
    }
    Log.e(ConversationActivity::class.simpleName, "buttonContainer()")
    buttonCounter(number, onClick = {
        number = number.plus(1)
    })
}

@Composable
fun buttonCounter(count : Int, onClick : () -> Unit) {
    Log.e(ConversationActivity::class.simpleName, "buttonCounter()")
    Button(
        onClick = onClick,
        content = { Text(text = "number of click $count") }
    )
}

@Composable
fun Conversation(message : List<Message>) {
    Log.e(ConversationActivity::class.simpleName, "Conversation()")
    LazyColumn() {
        items(message) { message ->
            MessageCard(message)
        }
    }
}

@Composable
fun MessageCard(msg : Message) {
    var extended by remember {
        mutableStateOf(msg.extended)
    }
    val surfaceColor by animateColorAsState(
        if (extended) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Row(modifier = Modifier.padding(5.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Icon",
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterVertically)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(modifier = Modifier
            .padding(5.dp)
            .clickable {
                val newState = !extended
                msg.extended = newState
                extended = newState
            }) {
            Text(
                text = msg.title,
                Modifier.fillMaxWidth(1.0f),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(5.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.message,
                    Modifier
                        .fillMaxWidth(1.0f)
                        .padding(5.dp),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = if (extended) Int.MAX_VALUE else 1
                )
            }
        }
    }
}

@Preview(name = "List Light preview", uiMode = UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun defaultLightPreview() {
    body()
}

@Preview(name = "List Dark preview", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun defaultDarkPreview() {
    body()
}
