package com.my.composeapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.internal.managers.ViewComponentManager

private val DarkColorScheme = darkColorScheme(
    primary = RED_POINT,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    onPrimary = White, // text color
    surface = GRAY_REAL,
)

private val LightColorScheme = lightColorScheme(
    primary = Red,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    onPrimary = White, // text color
    surface = GRAY_REAL,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ComposeApplicationTheme(
    darkTheme : Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor : Boolean = true,
    content : @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            if (darkTheme) DarkColorScheme else LightColorScheme
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = when (val context = view.context) {
                is ViewComponentManager.FragmentContextWrapper -> {
                    context.baseContext as Activity
                }
                is Activity -> {
                    context
                }
                else -> {
                    null
                }
            }
            activity?.window?.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}