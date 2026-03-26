package com.neonhabit.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity для задач
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val categoryId: Long? = null,
    val tagIds: String = "", // JSON массив ID
    val dueDate: Long? = null,
    val reminderTime: Long? = null,
    val isCompleted: Boolean = false,
    val isPrivate: Boolean = false,
    val completedAt: Long? = null,
    val expReward: Int = 10,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
