package com.neonhabit.app.ui.screens.addtask

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neonhabit.app.ui.components.NeonButton
import com.neonhabit.app.ui.components.NeonOutlineButton
import com.neonhabit.app.ui.theme.*

/**
 * Модель состояния экрана добавления задачи
 */
data class AddTaskUiState(
    val title: String = "",
    val description: String = "",
    val note: String = "",
    val selectedCategoryId: Long? = null,
    val selectedTagIds: List<Long> = emptyList(),
    val dueDate: Long? = null,
    val reminderTime: Long? = null,
    val isPrivate: Boolean = false,
    val expReward: Int = 10,
    val isSaving: Boolean = false,
    val error: String? = null,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false
)

/**
 * Экран добавления/редактирования задачи
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onNavigateBack: () -> Unit,
    onTaskSaved: () -> Unit,
    viewModel: AddTaskViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeonBlack)
    ) {
        // Фоновый эффект
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        listOf(
                            NeonPurple.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = NeonWhite
                    )
                }
                
                Text(
                    text = "Новая задача",
                    color = NeonWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                
                Spacer(modifier = Modifier.size(48.dp))
            }
            
            // Поле названия
            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::updateTitle,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Название *", color = NeonWhite50) },
                placeholder = { Text("Введите название задачи", color = NeonWhite50) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPink,
                    unfocusedBorderColor = NeonLightGray,
                    focusedTextColor = NeonWhite,
                    unfocusedTextColor = NeonWhite,
                    cursorColor = NeonPink,
                    focusedLabelColor = NeonPink,
                    unfocusedLabelColor = NeonWhite50
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = false,
                minLines = 1,
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Поле описания
            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::updateDescription,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Описание", color = NeonWhite50) },
                placeholder = { Text("Добавьте описание задачи", color = NeonWhite50) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = NeonLightGray,
                    focusedTextColor = NeonWhite,
                    unfocusedTextColor = NeonWhite,
                    cursorColor = NeonCyan,
                    focusedLabelColor = NeonCyan,
                    unfocusedLabelColor = NeonWhite50
                ),
                shape = RoundedCornerShape(12.dp),
                minLines = 2,
                maxLines = 5
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Заметка (Markdown)
            NoteSection(
                note = uiState.note,
                onNoteChange = viewModel::updateNote
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Категория
            CategorySection(
                selectedCategoryId = uiState.selectedCategoryId,
                onCategorySelected = viewModel::selectCategory
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Теги
            TagsSection(
                selectedTagIds = uiState.selectedTagIds,
                onTagToggle = viewModel::toggleTag
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Дата выполнения
            DueDateSection(
                dueDate = uiState.dueDate,
                onDateSelected = { viewModel.updateDueDate(it) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Напоминание
            ReminderSection(
                reminderTime = uiState.reminderTime,
                onReminderSet = { viewModel.updateReminderTime(it) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Приватная задача
            PrivateTaskSection(
                isPrivate = uiState.isPrivate,
                onToggle = viewModel::togglePrivate
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Награда EXP
            ExpRewardSection(
                expReward = uiState.expReward,
                onExpChange = viewModel::updateExpReward
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Кнопки действий
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NeonOutlineButton(
                    onClick = onNavigateBack,
                    text = "Отмена",
                    modifier = Modifier.weight(1f),
                    borderColor = NeonLightGray,
                    textColor = NeonWhite50
                )
                
                NeonButton(
                    onClick = {
                        viewModel.saveTask()
                        onTaskSaved()
                    },
                    text = "Сохранить",
                    modifier = Modifier.weight(1f),
                    enabled = uiState.title.isNotBlank() && !uiState.isSaving,
                    colors = listOf(NeonPink, NeonPurple)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Секция заметки
 */
@Composable
fun NoteSection(
    note: String,
    onNoteChange: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NeonDarkGray
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Note,
                        contentDescription = null,
                        tint = NeonYellow,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Заметка (Markdown)",
                        color = NeonWhite,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
                
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Свернуть" else "Развернуть",
                        tint = NeonWhite50
                    )
                }
            }
            
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = onNoteChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "**Жирный**, *курсив*, -зачеркнутый-\n- Список\n`код`",
                            color = NeonWhite50,
                            fontSize = 14.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonYellow,
                        unfocusedBorderColor = NeonLightGray,
                        focusedTextColor = NeonWhite,
                        unfocusedTextColor = NeonWhite
                    ),
                    shape = RoundedCornerShape(8.dp),
                    minLines = 4,
                    maxLines = 8
                )
            }
        }
    }
}

/**
 * Секция выбора категории
 */
@Composable
fun CategorySection(
    selectedCategoryId: Long?,
    onCategorySelected: (Long) -> Unit
) {
    // Демо категории
    val categories = listOf(
        CategoryItem(1, "Работа", NeonPink),
        CategoryItem(2, "Здоровье", NeonGreen),
        CategoryItem(3, "Обучение", NeonCyan),
        CategoryItem(4, "Хобби", NeonPurple),
        CategoryItem(5, "Дом", NeonOrange),
        CategoryItem(6, "Финансы", NeonYellow)
    )
    
    Text(
        text = "Категория",
        color = NeonWhite,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = selectedCategoryId == category.id
            
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category.id) },
                label = {
                    Text(
                        text = category.name,
                        color = if (isSelected) NeonBlack else NeonWhite,
                        fontSize = 14.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = category.color,
                    containerColor = NeonDarkGray,
                    labelColor = NeonWhite
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

data class CategoryItem(
    val id: Long,
    val name: String,
    val color: Color
)

/**
 * Секция выбора тегов
 */
@Composable
fun TagsSection(
    selectedTagIds: List<Long>,
    onTagToggle: (Long) -> Unit
) {
    // Демо теги
    val tags = listOf(
        TagItem(1, "Срочно", NeonRed),
        TagItem(2, "Важно", NeonOrange),
        TagItem(3, "Быстро", NeonGreen),
        TagItem(4, "Долгосрок", NeonBlue)
    )
    
    Text(
        text = "Теги",
        color = NeonWhite,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            val isSelected = selectedTagIds.contains(tag.id)
            
            AssistChip(
                onClick = { onTagToggle(tag.id) },
                label = {
                    Text(
                        text = tag.name,
                        color = if (isSelected) NeonBlack else tag.color,
                        fontSize = 14.sp
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected) tag.color else NeonDarkGray,
                    labelColor = NeonWhite
                ),
                shape = RoundedCornerShape(16.dp),
                border = AssistChipDefaults.assistChipBorder(
                    borderColor = tag.color,
                    enabled = true
                )
            )
        }
    }
}

data class TagItem(
    val id: Long,
    val name: String,
    val color: Color
)

/**
 * Секция выбора даты
 */
@Composable
fun DueDateSection(
    dueDate: Long?,
    onDateSelected: (Long?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NeonDarkGray
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { /* Показать DatePicker */ },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = NeonPink,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "Дата выполнения",
                        color = NeonWhite,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = dueDate?.let { formatTimestamp(it) } ?: "Не выбрана",
                        color = NeonWhite50,
                        fontSize = 14.sp
                    )
                }
            }
            
            if (dueDate != null) {
                IconButton(onClick = { onDateSelected(null) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Очистить",
                        tint = NeonWhite50
                    )
                }
            }
        }
    }
}

/**
 * Секция напоминания
 */
@Composable
fun ReminderSection(
    reminderTime: Long?,
    onReminderSet: (Long?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NeonDarkGray
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { /* Показать TimePicker */ },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "Напоминание",
                        color = NeonWhite,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = reminderTime?.let { formatTimestamp(it) } ?: "Не установлено",
                        color = NeonWhite50,
                        fontSize = 14.sp
                    )
                }
            }
            
            if (reminderTime != null) {
                IconButton(onClick = { onReminderSet(null) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Очистить",
                        tint = NeonWhite50
                    )
                }
            }
        }
    }
}

/**
 * Секция приватной задачи
 */
@Composable
fun PrivateTaskSection(
    isPrivate: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NeonDarkGray
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onToggle() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isPrivate) NeonPurple else NeonWhite50,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "Приватная задача",
                        color = NeonWhite,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Требует биометрии для просмотра",
                        color = NeonWhite50,
                        fontSize = 14.sp
                    )
                }
            }
            
            Switch(
                checked = isPrivate,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeonBlack,
                    checkedTrackColor = NeonPurple,
                    uncheckedThumbColor = NeonWhite50,
                    uncheckedTrackColor = NeonLightGray
                )
            )
        }
    }
}

/**
 * Секция выбора награды EXP
 */
@Composable
fun ExpRewardSection(
    expReward: Int,
    onExpChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = NeonDarkGray
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = NeonYellow,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Награда: $expReward EXP",
                    color = NeonWhite,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Slider(
                value = expReward.toFloat(),
                onValueChange = { onExpChange(it.toInt()) },
                valueRange = 5f..50f,
                steps = 9,
                colors = SliderDefaults.colors(
                    thumbColor = NeonYellow,
                    activeTrackColor = NeonYellow,
                    inactiveTrackColor = NeonLightGray
                )
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("5", color = NeonWhite50, fontSize = 12.sp)
                Text("25", color = NeonWhite50, fontSize = 12.sp)
                Text("50", color = NeonWhite50, fontSize = 12.sp)
            }
        }
    }
}

/**
 * Форматирование timestamp
 */
fun formatTimestamp(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("dd MMMM yyyy, HH:mm", java.util.Locale("ru"))
    return format.format(date)
}
