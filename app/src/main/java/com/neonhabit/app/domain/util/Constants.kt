package com.neonhabit.app.domain.util

/**
 * Константы приложения
 */
object Constants {
    // Настройки уровня
    const val BASE_EXP = 100
    const val EXP_MULTIPLIER = 1.5
    
    // Настройки жизней
    const val MAX_LIVES = 3
    const val LIVES_REGEN_HOURS = 24
    
    // Помодоро
    const val POMODORO_FOCUS_MINUTES = 25
    const val POMODORO_BREAK_MINUTES = 5
    const val POMODORO_LONG_BREAK_MINUTES = 15
    const val POMODORO_SESSIONS_BEFORE_LONG_BREAK = 4
    
    // Уведомления
    const val NOTIFICATION_CHANNEL_ID = "neonhabit_reminders"
    const val NOTIFICATION_CHANNEL_NAME = "Напоминания о привычках"
    const val NOTIFICATION_ID_PREFIX = "neonhabit_notification_"
    
    // DataStore
    const val PREFERENCES_NAME = "neonhabit_preferences"
    const val ONBOARDING_COMPLETED_KEY = "onboarding_completed"
    const val BIOMETRIC_ENABLED_KEY = "biometric_enabled"
    const val SOUND_ENABLED_KEY = "sound_enabled"
    const val HAPTIC_ENABLED_KEY = "haptic_enabled"
    
    // Экспорт/Импорт
    const val EXPORT_FILE_NAME_PREFIX = "neonhabit_backup_"
    const val EXPORT_FILE_EXTENSION = ".json"
    
    // Теги и категории по умолчанию
    val DEFAULT_CATEGORIES = listOf(
        "Работа",
        "Здоровье",
        "Обучение",
        "Хобби",
        "Дом",
        "Финансы"
    )
    
    val DEFAULT_TAGS = listOf(
        "Срочно",
        "Важно",
        "Быстро",
        "Долгосрок"
    )
    
    // Цвета для категорий (неоновые)
    val NEON_COLORS = listOf(
        0xFFFF00FFL, // Neon Pink
        0xFF00FFFFL, // Cyan
        0xFF00FF00L, // Lime
        0xFFFFFF00L, // Yellow
        0xFFFF0080L, // Hot Pink
        0xFF8000FFL, // Purple
        0xFF0080FFL, // Blue
        0xFFFF8000L  // Orange
    )
}
