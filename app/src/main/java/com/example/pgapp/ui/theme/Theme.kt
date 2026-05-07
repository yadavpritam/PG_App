package com.example.pgapp.ui.theme

import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext

// 🔥 LIGHT THEME (HTML LOOK)
private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,

    background = Background,
    onBackground = OnSurface,

    surface = Surface,
    onSurface = OnSurface,

    surfaceVariant = PrimaryLight,
    onSurfaceVariant = OnSurfaceVariant
)

// 🌙 DARK THEME (balanced, same feel)
private val DarkColors = darkColorScheme(
    primary = Color(0xFF8AB4F8),

    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),

    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFB0B3B8)
)

@Composable
fun PGAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}