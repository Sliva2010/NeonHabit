package com.neonhabit.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity для привычек
 */
@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val categoryId: Long? = null,
    val tagIds: String = "", // JSON массив ID
    val frequency: String = "DAILY", // DAILY, WEEKLY, CUSTOM
    val targetCount: Int = 1,
    val scheduledTime: Long? = null,
    val isFrozen: Boolean = false,
    val frozenUntil: Long? = null,
    val lives: Int = 3,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val totalCompletions: Int = 0,
    val expReward: Int = 20,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
