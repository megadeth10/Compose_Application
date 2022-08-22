package com.my.composeapplication.scene.basiclayout

import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.my.composeapplication.R
import com.my.composeapplication.base.*
import com.my.composeapplication.scene.basiclayout.data.AlignBodyItem
import com.my.composeapplication.viewmodel.LayoutComposeViewModel
import com.skydoves.landscapist.rememberDrawablePainter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * Created by YourName on 2022/08/18.
 */
@AndroidEntryPoint
class LayoutComposeActivity : BaseComponentActivity() {
    private val viewModel by viewModels<LayoutComposeViewModel>()
    override fun getContent() : @Composable () -> Unit = {
        MainScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbarHostState = null
    }
}

private var snackbarHostState : SnackbarHostState? = null

private val sampleData = listOf(
    AlignBodyItem(R.drawable.ic_baseline_sentiment_dissatisfied_24, "1111"),
    AlignBodyItem(R.drawable.ic_baseline_sentiment_satisfied_alt_24, "222222222"),
    AlignBodyItem(R.drawable.ic_baseline_sentiment_very_dissatisfied_24, "333"),
    AlignBodyItem(R.drawable.ic_baseline_sentiment_very_dissatisfied_24, "lkljasrsgsfgdfadsf"),
    AlignBodyItem(R.drawable.ic_launcher_foreground, "1231231231sfgsfdgsdfg2"),
)

sealed class BottomNavigationScreens(val route : String, @StringRes val resourceId : Int, val icon : ImageVector) {
    object Frankendroid : BottomNavigationScreens("Frankendroid", R.string.level1, Icons.Filled.Terrain)
    object Pumpkin : BottomNavigationScreens("Pumpkin", R.string.level2, Icons.Filled.FoodBank)
    object Ghost : BottomNavigationScreens("Ghost", R.string.level3, Icons.Filled.Fireplace)
    object ScaryBag : BottomNavigationScreens("ScaryBag", R.string.result, Icons.Filled.Cake)
}

private val bottomItems = listOf(
    BottomNavigationScreens.Frankendroid,
    BottomNavigationScreens.Pumpkin,
    BottomNavigationScreens.Ghost,
    BottomNavigationScreens.ScaryBag
)

@Composable
private fun MainScreen() {
    val navController = rememberNavController()
    snackbarHostState = customScaffold(
        bottomAppBar = { SootheBottomNavigation(bottomItems, navController) }
    ) {
        val backStackState = navController.currentBackStackEntryAsState()
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(it)
        ) {
            NavHost(
                navController = navController, startDestination = BottomNavigationScreens.Frankendroid.route
            ) {
                composable(BottomNavigationScreens.Frankendroid.route) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        ScrollBoxesSmooth()
                        XmlDrawable()
                        SearchBar(modifier = Modifier.padding(8.dp))
                        SectionTitle(R.string.body_section_title) {
                            AlignBodyRow(data = sampleData, scrollStateKeyName = "bbb")
                        }
                        SectionTitle(R.string.body_section_title) {
                            FavoriteCollectionGrid(sampleData)
                        }
                        FavoriteCollectionCard(
                            data = AlignBodyItem(imageId = R.drawable.ic_baseline_sentiment_dissatisfied_24, text = "aaaa")
                        )
                        SectionTitle(R.string.body_section_title) {
                            AlignBodyRow(data = sampleData, scrollStateKeyName = "ccc")
                        }
                        SectionTitle(R.string.body_section_title) {
                            FavoriteCollectionGrid(sampleData)
                        }
                    }
                }
                composable(BottomNavigationScreens.Pumpkin.route) {
                    Column {
                        SearchBar(modifier = Modifier.padding(8.dp))
                    }
                }
                composable(BottomNavigationScreens.Ghost.route) {
                    Column {
                        SectionTitle(R.string.body_section_title) {
                            AlignBodyRow(data = sampleData)
                        }
                    }
                }
                composable(BottomNavigationScreens.ScaryBag.route) {
                    Column {
                        SectionTitle(R.string.body_section_title) {
                            FavoriteCollectionGrid(sampleData)
                        }
                    }
                }
            }
            if (backStackState.value?.destination?.route == BottomNavigationScreens.Frankendroid.route) {
                CloseToastHoisting(snackbarHost = snackbarHostState)
            } else {
                BackButton(navController)
            }
        }
    }
}

@Composable
private fun ScrollBoxesSmooth() {
    // Smoothly scroll 100px on first composition
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }

    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .size(100.dp)
            .padding(horizontal = 8.dp)
            .verticalScroll(state)
    ) {
        repeat(10) {
            Text("Item $it", modifier = Modifier.padding(2.dp))
        }
    }
}

/**
 * Vector아닌 xml drawable은 이미지 loader를 이용한다.
 */
@Composable
fun XmlDrawable(modifier : Modifier = Modifier) {
    Image(
        painter = rememberDrawablePainter(
            ContextCompat.getDrawable(
                LocalContext.current,
                R.drawable.outline_bk_point_2
            )
        ),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    )
}

@Composable
fun BackButton(navController : NavHostController) {
    BackPressHandler {
        if (navController.currentDestination?.route != BottomNavigationScreens.Frankendroid.route) {
            navController.navigate(BottomNavigationScreens.Frankendroid.route)
        }
    }
}

@Composable
fun SectionTitle(strId : Int, modifier : Modifier = Modifier, content : @Composable () -> Unit) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = strId).uppercase(Locale.getDefault()),
            modifier = Modifier
                .paddingFromBaseline(bottom = 8.dp, top = 40.dp)
                .padding(8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        content()
    }
}

@Composable
fun FavoriteCollectionGrid(data : List<AlignBodyItem>, modifier : Modifier = Modifier) {
    val scrollState = rememberLazyGridState()
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        state = scrollState,
        modifier = modifier.height(112.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(data) { item ->
            FavoriteCollectionCard(data = item)
        }
    }
}

@Composable
fun AlignBodyRow(
    modifier : Modifier = Modifier,
    data : List<AlignBodyItem>,
    scrollStateKeyName : String = "aaa"
) {
//    val scrollState = rememberLazyListState()
    val viewModel : LayoutComposeViewModel = viewModel(
        LocalContext.current as LayoutComposeActivity
    )
    val scrollState = rememberForeverLazyListState(
        key = scrollStateKeyName,
        scrollParam = viewModel.getScrollState(scrollStateKeyName)
    ) { key, index, offset ->
        viewModel.setScrollState(key, ScrollStateParam(key, index, offset))
    }
    Surface(modifier = modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = scrollState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
        ) {
            items(data) { item ->
                AlignYourBodyElement(
                    data = item
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier : Modifier = Modifier) {
    var textState by rememberSaveable {
        mutableStateOf("")
    }
    TextField(
        value = textState,
        onValueChange = { textState = it },
        modifier = modifier
            .heightIn(56.dp)
            .fillMaxWidth(),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        placeholder = {
            Text(stringResource(id = R.string.search))
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun AlignYourBodyElement(modifier : Modifier = Modifier, data : AlignBodyItem) {
    val viewWidth = 88.dp
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            painterResource(id = data.imageId),
            modifier = Modifier
                .size(viewWidth)
                .clip(CircleShape)
                .align(CenterHorizontally),
            contentDescription = "AlignBodyItem",
            contentScale = ContentScale.Crop
        )
        Text(
            text = data.text,
            modifier = Modifier
                .width(viewWidth)
                .paddingFromBaseline(bottom = 8.dp, top = 24.dp),
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FavoriteCollectionCard(modifier : Modifier = Modifier, data : AlignBodyItem) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .width(192.dp)
                .height(56.dp)
                .background(Color(red = 214, green = 200, blue = 156, alpha = 255)),
            verticalAlignment = CenterVertically
        ) {
            Image(
                painterResource(id = data.imageId),
                modifier = Modifier.size(56.dp),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                text = data.text,
                modifier = Modifier
                    .width(0.dp)
                    .fillMaxHeight()
                    .weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun currentRoute(navController : NavHostController) : String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
private fun SootheBottomNavigation(
    items : List<BottomNavigationScreens>,
    navController : NavHostController,
    modifier : Modifier = Modifier
) {
    BottomNavigation(
        modifier,
        backgroundColor = MaterialTheme.colorScheme.background
    ) {
        val currentRoute = currentRoute(navController)
        items.forEach {
            val selected = currentRoute == it.route
            val color = if (selected) Color.Red else Color.Gray
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(it.route)
                    }
                },
                icon = {
                    Icon(
                        it.icon,
                        contentDescription = null,
                        tint = color
                    )
                },
                label = {
                    Text(stringResource(id = it.resourceId), color = color)
                },
                alwaysShowLabel = false,
                selectedContentColor = Color.Red,
                unselectedContentColor = Color.Gray
            )
        }
    }
}

@Preview(name = "XmlDrawable")
@Composable
fun XmlDrawablePreview() {
    XmlDrawable()
}

//@Preview(name = "SootheBottomNavigation")
//@Composable
//fun SootheBottomNavigationPreview() {
//    SootheBottomNavigation(bottomItems, NavHostController(LocalContext.current))
//}
//
//@Preview(name = "SectionTitle")
//@Composable
//fun SectionTitlePreview() {
//    SectionTitle(strId = R.string.level3) {
//
//    }
//}
//
//@Preview(name = "FavoriteCollectionGrid")
//@Composable
//fun FavoriteCollectionGridPreview() {
//    FavoriteCollectionGrid(sampleData)
//}
//
//@Preview(name = "FavoriteCollectionCardRow")
//@Composable
//fun FavoriteCollectionCardRowPreview() {
//    AlignBodyRow(data = sampleData)
//}
//
//@Preview(name = "FavoriteCollectionCard")
//@Composable
//fun FavoriteCollectionCardPreview() {
//    FavoriteCollectionCard(
//        modifier = Modifier.padding(8.dp),
//        data = AlignBodyItem(imageId = R.drawable.ic_baseline_sentiment_dissatisfied_24, text = "aaaa")
//    )
//}
//
//@Preview(name = "SearchBar")
//@Composable
//fun SearchBarPreview() {
//    SearchBar(modifier = Modifier.padding(8.dp))
//}
//
//@Preview(name = "AlignYourBodyElement")
//@Composable
//fun AlignYourBodyElementPreview() {
//    AlignYourBodyElement(
//        data = AlignBodyItem(imageId = R.drawable.ic_baseline_sentiment_dissatisfied_24, text = "aaaa")
//    )
//}
//
@Preview(name = "Screen")
@Composable
fun MySoothePreview() {
    MainScreen()
}