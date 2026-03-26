package com.neonhabit.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neonhabit.app.domain.model.Habit
import com.neonhabit.app.domain.model.HabitFrequency
import com.neonhabit.app.ui.theme.*

/**
 * Карточка привычки с неоновым стилем
 */
@Composable
fun HabitCard(
    habit: Habit,
    todayProgress: Int,
    onIncrement: (Habit) -> Unit,
    onEdit: (Habit) -> Unit = {},
    modifier: Modifier = Modifier,
    categoryColor: Color = NeonCyan
) {
    val hapticFeedback = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    val isCompleted = todayProgress >= habit.targetCount
    val progressPercent = todayProgress.toFloat() / habit.targetCount.toFloat()
    
    Box(
        modifier = modifier
            .scale(scale)
            .graphicsLayer { this.alpha = if (habit.isFrozen) 0.5f else 1f }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeonDarkGray)
                .clickable { }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = habit.title,
                            color = if (habit.isFrozen) NeonWhite50 else NeonWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        if (habit.isFrozen) {
                            Text(
                                text = "🧊",
                                fontSize = 16.sp
                            )
                        }
                    }
                    
                    if (habit.description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = habit.description,
                            color = NeonWhite50,
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Прогресс за сегодня
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Счётчик
                        Text(
                            text = "$todayProgress / ${habit.targetCount}",
                            color = categoryColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        
                        // Серия
                        if (habit.streak > 0) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🔥",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${habit.streak} дн.",
                                    color = NeonOrange,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        
                        // Жизни
                        LivesIndicator(habit.lives, maxLives = 3)
                    }
                }
                
                // Кнопка выполнения
                if (!habit.isFrozen) {
                    NeonIconButton(
                        onClick = {
                            isPressed = true
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                            onIncrement(habit)
                        },
                        enabled = !isCompleted,
                        icon = if (isCompleted) "✓" else "+",
                        color = categoryColor
                    )
                }
            }
            
            // Прогресс-бар
            Spacer(modifier = Modifier.height(12.dp))
            NeonProgressBar(
                progress = progressPercent,
                colors = listOf(categoryColor, categoryColor.copy(alpha = 0.5f)),
                height = 6.dp
            )
        }
        
        // Неоновая граница
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(categoryColor.copy(alpha = 0.2f), Color.Transparent)
                    )
                )
        )
    }
}

/**
 * Кнопка-иконка для привычек
 */
@Composable
fun NeonIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: String = "+",
    color: Color = NeonPink,
    size: Dp = 40.dp
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (enabled) Brush.horizontalGradient(listOf(color, color.copy(alpha = 0.7f)))
                else Brush.horizontalGradient(listOf(NeonLightGray, NeonMediumGray))
            )
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            ) {
                isPressed = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            color = if (enabled) NeonBlack else NeonWhite50,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

/**
 * Частота повторения в виде текста
 */
fun HabitFrequency.toDisplayText(): String {
    return when (this) {
        HabitFrequency.DAILY -> "Ежедневно"
        HabitFrequency.WEEKLY -> "Еженедельно"
        HabitFrequency.CUSTOM -> "Пользовательская"
    }
}
