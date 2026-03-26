package com.neonhabit.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity для категорий
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: Long,
    val icon: String = "default",
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Entity для тегов
 */
@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: Long,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Entity для прогресса привычек
 */
@Entity(tableName = "habit_progress")
data class HabitProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val habitId: Long,
    val date: Long,
    val completedCount: Int = 0,
    val isCompleted: Boolean = false,
    val missed: Boolean = false
)

/**
 * Entity для пользователя
 */
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1, // Всегда 1, один пользователь
    val level: Int = 1,
    val currentExp: Int = 0,
    val totalExp: Int = 0,
    val lives: Int = 3,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Entity для наград
 */
@Entity(tableName = "rewards")
data class RewardEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val description: String,
    val icon: String,
    val requiredLevel: Int,
    val requiredExp: Int = 0,
    val requiredStreak: Int = 0,
    val requiredCompletions: Int = 0,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val rarity: String = "COMMON"
)
