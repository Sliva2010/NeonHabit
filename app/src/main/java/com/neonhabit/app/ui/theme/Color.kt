package com.neonhabit.app.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// Основные цвета (AMOLED Black палитра)
// ==========================================

/**
 * Полностью черный для AMOLED экранов
 */
val NeonBlack = Color(0xFF000000)

/**
 * Очень темный серый для карточек и поверхностей
 */
val NeonDarkGray = Color(0xFF0A0A0A)

/**
 * Темный серый для элементов второго уровня
 */
val NeonMediumGray = Color(0xFF1A1A1A)

/**
 * Светлый серый для границ и разделителей
 */
val NeonLightGray = Color(0xFF2A2A2A)

/**
 * Белый с небольшой прозрачностью для текста
 */
val NeonWhite = Color(0xFFFFFFFF)
val NeonWhite70 = Color(0xB3FFFFFF)
val NeonWhite50 = Color(0x80FFFFFF)

// ==========================================
// Неоновые акцентные цвета
// ==========================================

/**
 * Основной неоновый цвет (розовый/маджента)
 */
val NeonPink = Color(0xFFFF00FF)
val NeonPinkGlow = Color(0x40FF00FF)

/**
 * Дополнительный неоновый цвет (циан)
 */
val NeonCyan = Color(0xFF00FFFF)
val NeonCyanGlow = Color(0x4000FFFF)

/**
 * Неоновый зеленый
 */
val NeonGreen = Color(0xFF00FF00)
val NeonGreenGlow = Color(0x4000FF00)

/**
 * Неоновый желтый
 */
val NeonYellow = Color(0xFFFFFF00)
val NeonYellowGlow = Color(0x40FFFF00)

/**
 * Неоновый оранжевый
 */
val NeonOrange = Color(0xFFFF8000)
val NeonOrangeGlow = Color(0x40FF8000)

/**
 * Неоновый фиолетовый
 */
val NeonPurple = Color(0xFF8000FF)
val NeonPurpleGlow = Color(0x408000FF)

/**
 * Неоновый синий
 */
val NeonBlue = Color(0xFF0080FF)
val NeonBlueGlow = Color(0x400080FF)

/**
 * Неоновый красный (для ошибок и жизней)
 */
val NeonRed = Color(0xFFFF0040)
val NeonRedGlow = Color(0x40FF0040)

// ==========================================
// Цвета для состояний
// ==========================================

val SuccessColor = NeonGreen
val WarningColor = NeonOrange
val ErrorColor = NeonRed
val InfoColor = NeonCyan

// ==========================================
// Градиенты
// ==========================================

val PinkCyanGradient = listOf(NeonPink, NeonPurple, NeonCyan)
val GreenCyanGradient = listOf(NeonGreen, NeonCyan)
val OrangePinkGradient = listOf(NeonOrange, NeonPink)
