package com.neonhabit.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Цветовая схема для темной темы (AMOLED Black)
 * Светлая тема намеренно не реализована - приложение только темное
 */
private val DarkColorScheme = darkColorScheme(
    primary = NeonPink,
    onPrimary = NeonBlack,
    primaryContainer = NeonPinkGlow,
    onPrimaryContainer = NeonWhite,
    
    secondary = NeonCyan,
    onSecondary = NeonBlack,
    secondaryContainer = NeonCyanGlow,
    onSecondaryContainer = NeonWhite,
    
    tertiary = NeonPurple,
    onTertiary = NeonBlack,
    tertiaryContainer = NeonPurpleGlow,
    onTertiaryContainer = NeonWhite,
    
    error = NeonRed,
    onError = NeonBlack,
    errorContainer = NeonRedGlow,
    onErrorContainer = NeonWhite,
    
    background = NeonBlack,
    onBackground = NeonWhite,
    
    surface = NeonDarkGray,
    onSurface = NeonWhite,
    surfaceVariant = NeonMediumGray,
    onSurfaceVariant = NeonWhite70,
    
    outline = NeonLightGray,
    outlineVariant = NeonMediumGray,
    
    inverseSurface = NeonWhite,
    inverseOnSurface = NeonBlack,
    
    inversePrimary = NeonPink,
    
    surfaceTint = NeonPink
)

/**
 * Светлая цветовая схема (НЕ ИСПОЛЬЗУЕТСЯ)
 * Определена для совместимости, но приложение всегда использует темную тему
 */
private val LightColorScheme = lightColorScheme(
    primary = NeonPink,
    onPrimary = NeonWhite,
    primaryContainer = NeonPink,
    onPrimaryContainer = NeonBlack,
    
    secondary = NeonCyan,
    onSecondary = NeonWhite,
    secondaryContainer = NeonCyan,
    onSecondaryContainer = NeonBlack,
    
    tertiary = NeonPurple,
    onTertiary = NeonWhite,
    tertiaryContainer = NeonPurple,
    onTertiaryContainer = NeonBlack,
    
    error = NeonRed,
    onError = NeonWhite,
    errorContainer = NeonRed,
    onErrorContainer = NeonBlack,
    
    background = Color(0xFFF5F5F5),
    onBackground = Color(0xFF1C1B1F),
    
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    
    outline = Color(0xFF79747E)
)

/**
 * Тема приложения NeonHabit
 * 
 * @param darkTheme Всегда true - приложение только в темной теме
 * @param dynamicColor Использовать динамические цвета Android 12+
 * @param content Контент приложения
 */
@Composable
fun NeonHabitTheme(
    darkTheme: Boolean = true, // Всегда темная тема
    dynamicColor: Boolean = false, // Отключаем динамические цвета для сохранения неоновой палитры
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Устанавливаем черный цвет статус бара и навигации
            window.statusBarColor = NeonBlack.toArgb()
            window.navigationBarColor = NeonBlack.toArgb()
            
            // Устанавливаем светлые иконки статус бара
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Типографика Material Theme
 */
private val Typography = androidx.compose.material3.Typography(
    displayLarge = TitleLarge,
    displayMedium = TitleMedium,
    displaySmall = TitleSmall,
    
    headlineLarge = HeadlineLarge,
    headlineMedium = HeadlineMedium,
    headlineSmall = HeadlineSmall,
    
    titleLarge = TitleLarge,
    titleMedium = TitleMedium,
    titleSmall = TitleSmall,
    
    bodyLarge = BodyLarge,
    bodyMedium = BodyMedium,
    bodySmall = BodySmall,
    
    labelLarge = LabelLarge,
    labelMedium = LabelMedium,
    labelSmall = LabelSmall
)
