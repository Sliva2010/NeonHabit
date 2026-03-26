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
}
