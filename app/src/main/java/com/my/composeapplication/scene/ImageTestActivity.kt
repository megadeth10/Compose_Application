package com.my.composeapplication.scene

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.my.composeapplication.base.BaseComponentActivity
import com.my.composeapplication.viewmodel.ImageTestViewModel
import com.my.composeapplication.R
import com.my.composeapplication.base.CustomTopAppBar
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by YourName on 2022/08/31.
 * App 외부에서 사진 이미지 또는 파일 가져오는 샘플
 */

@AndroidEntryPoint
class ImageTestActivity : BaseComponentActivity() {
    private val viewModel by viewModels<ImageTestViewModel>()
    override fun getContent() : @Composable () -> Unit = {
        ImageTextScreenHoisting()
    }
}

@Composable
fun ImageTextScreenHoisting() {
    val viewModel = viewModel<ImageTestViewModel>(LocalContext.current as BaseComponentActivity)
    ImageTestScreen(viewModel.imageData.value, viewModel::setImageData)
}

@Composable
fun ImageTestScreen(imageData : Any?, setImageData : (Any) -> Unit) {
    Column {
        TitleBarCompose(setImageData = setImageData)
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
        ) {
            val screenWidth = with(LocalDensity.current) {
                this@BoxWithConstraints.maxWidth.roundToPx()
            }
            val screenHeight = with(LocalDensity.current) {
                this@BoxWithConstraints.maxHeight.roundToPx()
            }
            val viewSize = Size(screenWidth, screenHeight)
//            ScollableText(
//                viewSize = viewSize,
//                modifier = Modifier.fillMaxSize(),
//            )
            ImageScreen(
                viewSize = viewSize,
                modifier = Modifier.fillMaxSize(),
                imageData = imageData
            )
        }
    }
}

@Composable
fun ScollableText(
    modifier : Modifier = Modifier,
    viewSize : Size,
) {
    val scrollState = rememberScrollState()
    val imageSize = Size(120, 160)
    val scaleHeight = scaleHeightCalculate(view = viewSize, image = imageSize)
    val heightDp = with(LocalDensity.current) {
        scaleHeight.toDp()
    }
    val scrollEnable = viewSize.height < scaleHeight
    Log.e("LEE", "ImageScreen image scale viewHeight: $viewSize height: $scaleHeight scrollable:$scrollEnable")
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(
                scrollState,
            )
            .background(Color.Black),
    ) {
        Text(
            text = "a\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\n" +
                    "a\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\n" +
                    "a\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\n" +
                    "a\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\n" +
                    "a\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\n" +
                    "a\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\n",
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(heightDp)

        )
    }
}

private fun getDropboxIMGSize(context : Context, uri : Uri): BitmapFactory.Options {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeStream(
        context.contentResolver.openInputStream(uri),
        null,
        options
    )
    return options
}

@Composable
fun ImageScreen(
    modifier : Modifier = Modifier,
    viewSize : Size,
    imageData : Any?
) {
    imageData?.let {
        val scrollState = rememberScrollState()
        var imageSize: Size? = null
        val image = when (imageData) {
            is Bitmap -> {
                imageSize = Size(imageData.width, imageData.height)
                imageData
            }
            is Uri -> {
                //TODO URI 통한 파일
                val options = getDropboxIMGSize(LocalContext.current, imageData)
                imageSize = Size(options.outWidth, options.outHeight)
                imageData
            }
            is String -> {
                imageData
            }
            else -> {
                null
            }
        }
        val scaleHeight = imageSize?.let {
            scaleHeightCalculate(view = viewSize, image = it)
        } ?: viewSize.height.toFloat()

        val heightDp = with(LocalDensity.current) {
            scaleHeight.toDp()
        }
        val scrollEnable = viewSize.height < scaleHeight
        GlideImage(
            imageModel = image,
            modifier = modifier
                .fillMaxWidth()
                .height(heightDp)
                .verticalScroll(
                    scrollState,
                    enabled = scrollEnable
                )
                .background(Color.Black),
            contentScale = ContentScale.FillWidth,
            previewPlaceholder = R.drawable.ic_launcher_foreground
        )
    }
}

@Composable
fun TitleBarCompose(setImageData : (Any) -> Unit) {
    val takePictureStartForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->
            result?.let {
                Log.e("LEE", "TakePicturePreview result() has Bitmap")
                setImageData(it)
            }
        }

    val pickImageStartForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let {
                Log.e("LEE", "TakePicturePreview result() has uri: $it")
                setImageData(it)
            }
        }

    CustomTopAppBar(
        title = "Image List",
        actions = {
            IconButton(onClick = {
                takePictureStartForResult.launch()
            }) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                pickImageStartForResult.launch("image/*")
            }) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null
                )
            }
        }
    )
}

private fun checkTopBottom(value1 : Int, value2 : Int) : Pair<Int, Int> {
    var topValue = value1
    var bottomValue = value2
    if (value1 > value2) {
        topValue = value2
        bottomValue = value1
    }
    return Pair(topValue, bottomValue)
}

// width 기준 height scale value 계산
private fun scaleHeightCalculate(view : Size, image : Size) : Float {
    val pairWidth = checkTopBottom(view.width, image.width)
    var pairHeight = checkTopBottom(view.height, image.height)

    val scaleWidth = pairWidth.second.toFloat() / pairWidth.first.toFloat()

    return image.height * scaleWidth
}

@Preview(name = "ImageTestScreen")
@Composable
fun ImageTestScreenPreview() {
    ImageTestScreen(
        imageData = "https://cdn.pixabay.com/photo/2020/07/14/16/18/snow-5404785_960_720.jpg",
        setImageData = {}
    )
}