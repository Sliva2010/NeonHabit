package com.neonhabit.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.neonhabit.app.data.local.database.HabitDatabase
import com.neonhabit.app.data.repository.HabitRepository
import com.neonhabit.app.data.repository.TaskRepository
import com.neonhabit.app.domain.util.Constants

/**
 * Главный класс приложения
 */
class NeonHabitApp : Application() {
    
    // База данных
    val database: HabitDatabase by lazy {
        HabitDatabase.getDatabase(this)
    }
    
    // Репозитории
    val taskRepository: TaskRepository by lazy {
        TaskRepository(database.taskDao())
    }
    
    val habitRepository: HabitRepository by lazy {
        HabitRepository(database.habitDao())
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // Создаем канал уведомлений
        createNotificationChannel()
        
        // Инициализируем данные по умолчанию
        initializeDefaultData()
    }
    
    /**
     * Создание канала уведомлений
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Напоминания о задачах и привычках"
                enableLights(true)
                enableVibration(true)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    /**
     * Инициализация данных по умолчанию (категории, теги, награды)
     */
    private fun initializeDefaultData() {
        // Запускаем в фоне
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            // Проверяем, есть ли уже категории
            val existingCategories = database.categoryDao().getAllCategories().first()
            if (existingCategories.isEmpty()) {
                // Создаем категории по умолчанию
                Constants.DEFAULT_CATEGORIES.forEachIndexed { index, name ->
                    database.categoryDao().insertCategory(
                        com.neonhabit.app.data.local.entity.CategoryEntity(
                            name = name,
                            color = Constants.NEON_COLORS[index % Constants.NEON_COLORS.size]
                        )
                    )
                }
            }
            
            // Проверяем, есть ли уже теги
            val existingTags = database.tagDao().getAllTags().first()
            if (existingTags.isEmpty()) {
                // Создаем теги по умолчанию
                Constants.DEFAULT_TAGS.forEachIndexed { index, name ->
                    database.tagDao().insertTag(
                        com.neonhabit.app.data.local.entity.TagEntity(
                            name = name,
                            color = Constants.NEON_COLORS[index % Constants.NEON_COLORS.size]
                        )
                    )
                }
            }
            
            // Проверяем профиль пользователя
            val userProfile = database.userProfileDao().getUserProfileOnce()
            if (userProfile == null) {
                database.userProfileDao().insertUserProfile(
                    com.neonhabit.app.data.local.entity.UserProfileEntity(
                        level = 1,
                        currentExp = 0,
                        totalExp = 0,
                        lives = 3
                    )
                )
            }
            
            // Инициализируем награды
            initializeRewards()
        }
    }
    
    /**
     * Инициализация наград
     */
    private suspend fun initializeRewards() {
        val existingRewards = database.rewardDao().getAllRewards().first()
        if (existingRewards.isEmpty()) {
            val rewards = listOf(
                com.neonhabit.app.data.local.entity.RewardEntity(
                    id = 1,
                    name = "Первый шаг",
                    description = "Выполните первую задачу",
                    icon = "🎯",
                    requiredLevel = 1,
                    requiredCompletions = 1,
                    rarity = "COMMON"
                ),
                com.neonhabit.app.data.local.entity.RewardEntity(
                    id = 2,
                    name = "Новичок",
                    description = "Достигните 5 уровня",
                    icon = "🌟",
                    requiredLevel = 5,
                    rarity = "COMMON"
                ),
                com.neonhabit.app.data.local.entity.RewardEntity(
                    id = 3,
                    name = "Серийный убийца",
                    description = "Серия из 7 дней",
                    icon = "🔥",
                    requiredStreak = 7,
                    rarity = "RARE"
                ),
                com.neonhabit.app.data.local.entity.RewardEntity(
                    id = 4,
                    name = "Мастер",
                    description = "Достигните 10 уровня",
                    icon = "👑",
                    requiredLevel = 10,
                    rarity = "EPIC"
                ),
                com.neonhabit.app.data.local.entity.RewardEntity(
                    id = 5,
                    name = "Легенда",
                    description = "Достигните 20 уровня",
                    icon = "💎",
                    requiredLevel = 20,
                    rarity = "LEGENDARY"
                )
            )
            rewards.forEach { database.rewardDao().insertReward(it) }
        }
    }
}