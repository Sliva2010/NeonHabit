package com.neonhabit.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Модель задачи
 */
@Serializable
data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val categoryId: Long? = null,
    val tagIds: List<Long> = emptyList(),
    val dueDate: Long? = null, // Timestamp
    val reminderTime: Long? = null, // Timestamp для напоминания
    val isCompleted: Boolean = false,
    val isPrivate: Boolean = false, // Требует биометрии
    val completedAt: Long? = null,
    val expReward: Int = 10,
    val note: String = "", // Markdown заметка
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Модель привычки
 */
@Serializable
data class Habit(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val categoryId: Long? = null,
    val tagIds: List<Long> = emptyList(),
    val frequency: HabitFrequency = HabitFrequency.DAILY,
    val targetCount: Int = 1, // Сколько раз в день/неделю
    val scheduledTime: Long? = null, // Время напоминания (часы в миллисекундах)
    val isFrozen: Boolean = false, // Заморожена ли привычка
    val frozenUntil: Long? = null, // До когда заморожена
    val lives: Int = 3, // Количество жизней
    val streak: Int = 0, // Текущая серия
    val bestStreak: Int = 0, // Лучшая серия
    val totalCompletions: Int = 0,
    val expReward: Int = 20,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Частота повторения привычки
 */
enum class HabitFrequency {
    DAILY,          // Ежедневно
    WEEKLY,         // Еженедельно
    CUSTOM          // Пользовательская (например, 3 раза в неделю)
}

/**
 * Модель категории
 */
@Serializable
data class Category(
    val id: Long = 0,
    val name: String,
    val color: Long, // ARGB цвет в Long
    val icon: String = "default",
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Модель тега
 */
@Serializable
data class Tag(
    val id: Long = 0,
    val name: String,
    val color: Long, // ARGB цвет в Long
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Прогресс выполнения привычки за день
 */
@Serializable
data class HabitProgress(
    val id: Long = 0,
    val habitId: Long,
    val date: Long, // Дата в timestamp (начало дня)
    val completedCount: Int = 0,
    val isCompleted: Boolean = false,
    val missed: Boolean = false // Пропущен ли день
)
