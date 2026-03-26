package com.neonhabit.app.data.repository

import com.neonhabit.app.data.local.dao.HabitDao
import com.neonhabit.app.data.local.entity.HabitEntity
import com.neonhabit.app.data.local.entity.HabitProgressEntity
import com.neonhabit.app.domain.model.Habit
import com.neonhabit.app.domain.model.HabitFrequency
import com.neonhabit.app.domain.model.HabitProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Репозиторий для операций с привычками
 */
class HabitRepository(private val habitDao: HabitDao) {
    
    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits().map { entities ->
        entities.map { it.toDomainModel() }
    }
    
    val activeHabits: Flow<List<Habit>> = habitDao.getActiveHabits().map { entities ->
        entities.map { it.toDomainModel() }
    }
    
    val frozenHabits: Flow<List<Habit>> = habitDao.getFrozenHabits().map { entities ->
        entities.map { it.toDomainModel() }
    }
    
    suspend fun getHabitById(id: Long): Habit? {
        return habitDao.getHabitById(id)?.toDomainModel()
    }
    
    fun getHabitsByCategory(categoryId: Long): Flow<List<Habit>> {
        return habitDao.getHabitsByCategory(categoryId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    suspend fun insertHabit(habit: Habit): Long {
        return habitDao.insertHabit(habit.toEntity())
    }
    
    suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit.toEntity())
    }
    
    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit.toEntity())
    }
    
    suspend fun deleteHabitById(id: Long) {
        habitDao.deleteHabitById(id)
    }
    
    suspend fun updateHabitStreak(id: Long, streak: Int, bestStreak: Int) {
        habitDao.updateHabitStreak(id, streak, bestStreak, System.currentTimeMillis())
    }
    
    suspend fun incrementCompletion(id: Long) {
        habitDao.incrementCompletion(id, System.currentTimeMillis())
    }
    
    suspend fun decrementLife(id: Long) {
        habitDao.decrementLife(id, System.currentTimeMillis())
    }
    
    suspend fun updateFreezeStatus(id: Long, frozen: Boolean, frozenUntil: Long?) {
        habitDao.updateFreezeStatus(id, frozen, frozenUntil, System.currentTimeMillis())
    }
    
    // Habit Progress
    fun getHabitProgress(habitId: Long): Flow<List<HabitProgress>> {
        return habitDao.getHabitProgress(habitId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    suspend fun getHabitProgressForDate(habitId: Long, date: Long): HabitProgress? {
        return habitDao.getHabitProgressForDate(habitId, date)?.toDomainModel()
    }
    
    fun getHabitProgressByDateRange(habitId: Long, startDate: Long, endDate: Long): Flow<List<HabitProgress>> {
        return habitDao.getHabitProgressByDateRange(habitId, startDate, endDate)
            .map { entities -> entities.map { it.toDomainModel() } }
    }
    
    suspend fun insertHabitProgress(progress: HabitProgress): Long {
        return habitDao.insertHabitProgress(progress.toEntity())
    }
    
    suspend fun updateHabitProgress(progress: HabitProgress) {
        habitDao.updateHabitProgress(progress.toEntity())
    }
    
    suspend fun deleteOldProgress(habitId: Long, cutoffDate: Long) {
        habitDao.deleteOldProgress(habitId, cutoffDate)
    }
    
    suspend fun getTotalHabitCompletions(): Int {
        return habitDao.getTotalHabitCompletions() ?: 0
    }
}

// Extension functions для маппинга
fun HabitEntity.toDomainModel(): Habit {
    return Habit(
        id = id,
        title = title,
        description = description,
        categoryId = categoryId,
        tagIds = tagIds.split(",").filter { it.isNotEmpty() }.map { it.toLong() },
        frequency = HabitFrequency.valueOf(frequency),
        targetCount = targetCount,
        scheduledTime = scheduledTime,
        isFrozen = isFrozen,
        frozenUntil = frozenUntil,
        lives = lives,
        streak = streak,
        bestStreak = bestStreak,
        totalCompletions = totalCompletions,
        expReward = expReward,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Habit.toEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        title = title,
        description = description,
        categoryId = categoryId,
        tagIds = tagIds.joinToString(","),
        frequency = frequency.name,
        targetCount = targetCount,
        scheduledTime = scheduledTime,
        isFrozen = isFrozen,
        frozenUntil = frozenUntil,
        lives = lives,
        streak = streak,
        bestStreak = bestStreak,
        totalCompletions = totalCompletions,
        expReward = expReward,
        createdAt = createdAt,
        updatedAt = System.currentTimeMillis()
    )
}

fun HabitProgressEntity.toDomainModel(): HabitProgress {
    return HabitProgress(
        id = id,
        habitId = habitId,
        date = date,
        completedCount = completedCount,
        isCompleted = isCompleted,
        missed = missed
    )
}

fun HabitProgress.toEntity(): HabitProgressEntity {
    return HabitProgressEntity(
        id = id,
        habitId = habitId,
        date = date,
        completedCount = completedCount,
        isCompleted = isCompleted,
        missed = missed
    )
}
