package com.neonhabit.app.domain.model

import kotlinx.serialization.Serializable
import kotlin.math.pow

/**
 * Модель уровня пользователя
 */
@Serializable
data class UserLevel(
    val level: Int = 1,
    val currentExp: Int = 0,
    val requiredExp: Int = 100,
    val totalExp: Int = 0
) {
    /**
     * Процент прогресса до следующего уровня (0..1)
     */
    val progressPercent: Float = currentExp.toFloat() / requiredExp.toFloat()

    /**
     * Добавить опыт и вернуть новый уровень с возможным повышением
     */
    fun addExp(amount: Int): Pair<UserLevel, Boolean> {
        var newExp = currentExp + amount
        var newTotalExp = totalExp + amount
        var newLevel = level
        var newRequiredExp = requiredExp
        var leveledUp = false

        while (newExp >= newRequiredExp) {
            newExp -= newRequiredExp
            newLevel++
            newRequiredExp = calculateRequiredExp(newLevel)
            leveledUp = true
        }

        return Pair(
            UserLevel(
                level = newLevel,
                currentExp = newExp,
                requiredExp = newRequiredExp,
                totalExp = newTotalExp
            ),
            leveledUp
        )
    }

    companion object {
        /**
         * Расчет необходимого опыта для уровня
         * Формула: 100 * level^1.5
         */
        fun calculateRequiredExp(level: Int): Int {
            return (100 * level.toDouble().pow(1.5)).toInt()
        }
    }
}

/**
 * Модель награды
 */
@Serializable
data class Reward(
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
    val rarity: RewardRarity = RewardRarity.COMMON
)

/**
 * Редкость награды
 */
enum class RewardRarity {
    COMMON,     // Обычная
    RARE,       // Редкая
    EPIC,       // Эпическая
    LEGENDARY   // Легендарная
}

/**
 * Достижение
 */
@Serializable
data class Achievement(
    val id: Long,
    val name: String,
    val description: String,
    val icon: String,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val progress: Int = 0,
    val target: Int
)
