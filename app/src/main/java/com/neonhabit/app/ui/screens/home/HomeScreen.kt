package com.neonhabit.app.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neonhabit.app.domain.model.Task
import com.neonhabit.app.ui.components.*
import com.neonhabit.app.ui.theme.*

/**
 * Главный экран приложения
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToHabits: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToPomodoro: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToAddTask: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeonBlack)
    ) {
        // Фоновый градиент
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(100.dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            NeonPink.copy(alpha = 0.15f),
                            NeonPurple.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Header с профилем
            HeaderSection(
                userLevel = uiState.userLevel,
                modifier = Modifier.padding(top = 16.dp)
            )
            
            // Быстрые действия
            QuickActionsSection(
                onNavigateToHabits = onNavigateToHabits,
                onNavigateToStats = onNavigateToStats,
                onNavigateToPomodoro = onNavigateToPomodoro,
                onNavigateToInventory = onNavigateToInventory,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            
            // Поиск
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::searchTasks,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Статистика дня
            DayStatsCard(
                pendingCount = uiState.pendingTasksCount,
                completedCount = uiState.completedTasksCount,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Список задач
            Text(
                text = "Задачи на сегодня",
                color = NeonWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = NeonPink,
                        modifier = Modifier.size(40.dp)
                    )
                }
            } else if (uiState.tasks.isEmpty()) {
                EmptyStateCard(
                    icon = "📝",
                    title = "Нет задач",
                    description = "Добавьте первую задачу и начните свой путь!",
                    actionText = "Добавить задачу",
                    onAction = onNavigateToAddTask
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.tasks,
                        key = { it.id }
                    ) { task ->
                        AnimatedTaskItem(
                            task = task,
                            onToggleComplete = viewModel::toggleTaskCompletion,
                            onEdit = {},
                            onDelete = viewModel::deleteTask
                        )
                    }
                }
            }
        }
        
        // FAB для добавления задачи
        AnimatedVisibility(
            visible = !uiState.isLoading,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = onNavigateToAddTask,
                containerColor = NeonPink,
                contentColor = NeonBlack,
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить задачу",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        
        // Диалог повышения уровня
        if (uiState.showLevelUpDialog) {
            LevelUpDialog(
                onDismiss = { viewModel.refreshTasks() }
            )
        }
    }
}

/**
 * Header с информацией о пользователе
 */
@Composable
fun HeaderSection(
    userLevel: UserLevel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "NeonHabit",
                color = Brush.horizontalGradient(listOf(NeonPink, NeonPurple)),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Text(
                text = "Добрый день! 👋",
                color = NeonWhite50,
                fontSize = 14.sp
            )
        }
        
        // Мини-прогресс уровня
        LevelProgressBar(
            currentLevel = userLevel.level,
            currentExp = userLevel.currentExp,
            requiredExp = userLevel.requiredExp,
            modifier = Modifier.width(150.dp)
        )
    }
}

/**
 * Секция быстрых действий
 */
@Composable
fun QuickActionsSection(
    onNavigateToHabits: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToPomodoro: () -> Unit,
    onNavigateToInventory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionButton(
            icon = "🎯",
            label = "Привычки",
            onClick = onNavigateToHabits,
            colors = listOf(NeonCyan, NeonBlue)
        )
        QuickActionButton(
            icon = "📊",
            label = "Статистика",
            onClick = onNavigateToStats,
            colors = listOf(NeonGreen, NeonCyan)
        )
        QuickActionButton(
            icon = "⏱️",
            label = "Помодоро",
            onClick = onNavigateToPomodoro,
            colors = listOf(NeonOrange, NeonPink)
        )
        QuickActionButton(
            icon = "🏆",
            label = "Награды",
            onClick = onNavigateToInventory,
            colors = listOf(NeonPurple, NeonPink)
        )
    }
}

/**
 * Кнопка быстрого действия
 */
@Composable
fun QuickActionButton(
    icon: String,
    label: String,
    onClick: () -> Unit,
    colors: List<Color> = listOf(NeonPink, NeonPurple),
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    Column(
        modifier = modifier
            .weight(1f)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(colors)
            )
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = NeonBlack,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}

/**
 * Поисковая строка
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Поиск задач...",
                color = NeonWhite50
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск",
                tint = NeonWhite50
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonPink,
            unfocusedBorderColor = NeonLightGray,
            focusedTextColor = NeonWhite,
            unfocusedTextColor = NeonWhite,
            cursorColor = NeonPink
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

/**
 * Карточка статистики дня
 */
@Composable
fun DayStatsCard(
    pendingCount: Int,
    completedCount: Int,
    modifier: Modifier = Modifier
) {
    val total = pendingCount + completedCount
    val progress = if (total > 0) completedCount.toFloat() / total else 0f
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NeonDarkGray
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Прогресс дня",
                        color = NeonWhite,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$completedCount из $total выполнено",
                        color = NeonWhite50,
                        fontSize = 14.sp
                    )
                }
                
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = NeonPink,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            NeonProgressBar(
                progress = progress,
                colors = listOf(NeonGreen, NeonCyan),
                height = 8.dp
            )
        }
    }
}

/**
 * Анимированный элемент задачи
 */
@Composable
fun AnimatedTaskItem(
    task: Task,
    onToggleComplete: (Task) -> Unit,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }
    
    AnimatedVisibility(
        visible = isVisible,
        exit = fadeOut() + shrinkHorizontally(),
        modifier = Modifier.animateItemPlacement()
    ) {
        TaskCard(
            task = task,
            onToggleComplete = onToggleComplete,
            onEdit = onEdit,
            onDelete = {
                isVisible = false
                onDelete(it)
            },
            categoryColor = NeonPink
        )
    }
}

/**
 * Диалог повышения уровня
 */
@Composable
fun LevelUpDialog(
    onDismiss: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(0.5f) }
    
    LaunchedEffect(Unit) {
        animateFloat(
            initialValue = 0.5f,
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) { value ->
            scale = value
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(listOf(NeonPink, NeonPurple, NeonCyan))
                )
                .padding(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(NeonDarkGray)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉",
                    fontSize = 64.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "НОВЫЙ УРОВЕНЬ!",
                    color = Brush.horizontalGradient(listOf(NeonPink, NeonPurple)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Продолжай в том же духе!",
                    color = NeonWhite50,
                    fontSize = 16.sp
                )
            }
        }
    }
}
