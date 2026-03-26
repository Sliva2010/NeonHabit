package com.neonhabit.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neonhabit.app.ui.theme.*

/**
 * Неоновая кнопка с эффектом свечения
 */
@Composable
fun NeonButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    colors: List<Color> = listOf(NeonPink, NeonPurple),
    glowColor: Color = colors.first(),
    cornerRadius: Dp = 16.dp,
    padding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    showGlow: Boolean = true,
    haptic: Boolean = true
) {
    val hapticFeedback = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }
    
    // Анимация нажатия
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    // Анимация свечения
    val glowAlpha by animateFloatAsState(
        targetValue = if (isPressed && showGlow) 0.8f else 0.4f,
        animationSpec = tween(200)
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.horizontalGradient(
                    colors = if (enabled) colors else listOf(NeonLightGray, NeonMediumGray)
                )
            )
            .then(
                if (showGlow && enabled) {
                    Modifier.blur(20.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = colors.map { it.copy(alpha = glowAlpha) }
                            )
                        )
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.horizontalGradient(
                    colors = if (enabled) colors else listOf(NeonLightGray, NeonMediumGray)
                )
            )
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                isPressed = true
                if (haptic) hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (enabled) NeonBlack else NeonWhite50,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

/**
 * Неоновая кнопка с обводкой (outline)
 */
@Composable
fun NeonOutlineButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    borderColor: Color = NeonPink,
    textColor: Color = NeonPink,
    cornerRadius: Dp = 16.dp,
    padding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(cornerRadius))
            .drawBehind {
                drawRoundRect(
                    brush = Brush.horizontalGradient(listOf(borderColor, borderColor.copy(alpha = 0.5f))),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                isPressed = true
                onClick()
            }
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (enabled) textColor else NeonWhite50,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}

/**
 * Неоновая карточка с эффектом свечения
 */
@Composable
fun NeonCard(
    modifier: Modifier = Modifier,
    glowColor: Color = NeonPink,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp,
    showGlow: Boolean = true,
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val glowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.6f else 0.3f,
        animationSpec = tween(200)
    )
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .then(
                if (showGlow) {
                    Modifier.blur(15.dp)
                        .background(
                            glowColor.copy(alpha = glowAlpha),
                            RoundedCornerShape(cornerRadius)
                        )
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(cornerRadius))
            .drawBehind {
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        listOf(glowColor.copy(alpha = 0.3f), Color.Transparent)
                    ),
                    style = Stroke(width = borderWidth.toPx())
                )
            }
            .background(NeonDarkGray)
            .padding(16.dp)
    ) {
        content()
    }
}

/**
 * Компонент с неоновым текстом
 */
@Composable
fun NeonText(
    text: String,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(NeonPink, NeonPurple),
    fontSize: Dp = 24.dp,
    fontWeight: FontWeight = FontWeight.Bold,
    showGlow: Boolean = true
) {
    val glowAlpha by remember { mutableStateOf(0.5f) }
    
    Box(modifier = modifier) {
        // Слой свечения
        if (showGlow) {
            Text(
                text = text,
                color = colors.first().copy(alpha = glowAlpha),
                fontSize = fontSize,
                fontWeight = fontWeight,
                modifier = Modifier.blur(8.dp)
            )
        }
        // Основной текст
        Text(
            text = text,
            color = Brush.horizontalGradient(colors),
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    }
}
