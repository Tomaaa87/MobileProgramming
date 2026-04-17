package com.example.cookingtimer.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val SamsungDarkColorScheme = darkColorScheme(
    primary = SamsungPurple,           // Magenta/Purple for buttons
    onPrimary = Color.White,
    secondary = SamsungMediumGrey,
    background = SamsungBlack,         // True black background
    surface = SamsungSurface,          // Dark grey for cards
    onBackground = SamsungLightGrey,   // Off-white for text
    onSurface = SamsungLightGrey,
    outline = SamsungOutline,
    surfaceVariant = SamsungDarkGrey,
    onSurfaceVariant = SamsungLightGrey,
    primaryContainer = SamsungPurple,
    onPrimaryContainer = Color.White,
    secondaryContainer = SamsungDarkGrey,
    onSecondaryContainer = SamsungLightGrey
)

private val SamsungLightColorScheme = lightColorScheme(
    primary = SamsungPurple,
    secondary = SamsungMediumGrey,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = SamsungBlack,
    onSurface = SamsungBlack
)

@Composable
fun CookingtimerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> SamsungDarkColorScheme
        else -> SamsungLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}