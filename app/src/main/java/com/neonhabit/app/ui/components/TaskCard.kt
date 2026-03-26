package com.neonhabit.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neonhabit.app.domain.model.Task
import com.neonhabit.app.ui.theme.*

/**
 * Карточка задачи с неоновым стилем
 */
@Composable
fun TaskCard(
    task: Task,
    onToggleComplete: (Task) -> Unit,
    onEdit: (Task) -> Unit = {},
    onDelete: (Task) -> Unit = {},
    modifier: Modifier = Modifier,
    categoryColor: Color = NeonPink
) {
    val hapticFeedback = LocalHapticFeedback.current
    var isExpanded by remember { mutableStateOf(false) }
    
    // Анимация при выполнении
    val scale by animateFloatAsState(
        targetValue = if (task.isCompleted) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (task.isCompleted) 0.6f else 1f,
        animationSpec = tween(300)
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .graphicsLayer { this.alpha = alpha }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NeonDarkGray)
                .clickable { isExpanded = !isExpanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Checkbox с неоновым стилем
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onToggleComplete(task)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = categoryColor,
                            uncheckedColor = NeonLightGray
                        )
                    )
                    
                    // Задача
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.title,
                            color = if (task.isCompleted) NeonWhite50 else NeonWhite,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                        )
                        
                        if (task.description.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = task.description,
                                color = NeonWhite50,
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                
                // Индикатор приватности
                if (task.isPrivate) {
                    Text(
                        text = "🔒",
                        fontSize = 16.sp
                    )
                }
            }
            
            // Дополнительная информация
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Divider(color = NeonLightGray, thickness = 1.dp)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Заметка
                    if (task.note.isNotEmpty()) {
                        Text(
                            text = task.note,
                            color = NeonWhite70,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NeonMediumGray, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    // Действия
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onEdit(task) }) {
                            Text("Редактировать", color = NeonCyan, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = { onDelete(task) }) {
                            Text("Удалить", color = NeonRed, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
        
        // Неоновая граница
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(categoryColor.copy(alpha = 0.3f), Color.Transparent)
                    )
                )
        )
    }
}

/**
 * Карточка для пустого состояния
 */
@Composable
fun EmptyStateCard(
    icon: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 64.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = title,
            color = NeonWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = description,
            color = NeonWhite50,
            fontSize = 14.sp,
            maxLines = 3
        )
        
        if (actionText != null) {
            Spacer(modifier = Modifier.height(16.dp))
            NeonButton(
                onClick = onAction,
                text = actionText,
                colors = listOf(NeonPink, NeonPurple)
            )
        }
    }
}
