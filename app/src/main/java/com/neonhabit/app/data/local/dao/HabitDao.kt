package com.neonhabit.app.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.neonhabit.app.data.local.entity.HabitEntity
import com.neonhabit.app.data.local.entity.HabitProgressEntity

/**
 * DAO для операций с привычками
 */
@Dao
interface HabitDao {
    
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<HabitEntity>>
    
    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Long): HabitEntity?
    
    @Query("SELECT * FROM habits WHERE categoryId = :categoryId ORDER BY createdAt DESC")
    fun getHabitsByCategory(categoryId: Long): Flow<List<HabitEntity>>
    
    @Query("SELECT * FROM habits WHERE isFrozen = 0 ORDER BY streak DESC")
    fun getActiveHabits(): Flow<List<HabitEntity>>
    
    @Query("SELECT * FROM habits WHERE isFrozen = 1 ORDER BY frozenUntil ASC")
    fun getFrozenHabits(): Flow<List<HabitEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long
    
    @Update
    suspend fun updateHabit(habit: HabitEntity)
    
    @Delete
    suspend fun deleteHabit(habit: HabitEntity)
    
    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Long)
    
    @Query("UPDATE habits SET streak = :streak, bestStreak = :bestStreak, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateHabitStreak(id: Long, streak: Int, bestStreak: Int, updatedAt: Long)
    
    @Query("UPDATE habits SET totalCompletions = totalCompletions + 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun incrementCompletion(id: Long, updatedAt: Long)
    
    @Query("UPDATE habits SET lives = lives - 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun decrementLife(id: Long, updatedAt: Long)
    
    @Query("UPDATE habits SET isFrozen = :frozen, frozenUntil = :frozenUntil, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateFreezeStatus(id: Long, frozen: Boolean, frozenUntil: Long?, updatedAt: Long)
    
    // Habit Progress
    @Query("SELECT * FROM habit_progress WHERE habitId = :habitId ORDER BY date DESC")
    fun getHabitProgress(habitId: Long): Flow<List<HabitProgressEntity>>
    
    @Query("SELECT * FROM habit_progress WHERE habitId = :habitId AND date = :date")
    suspend fun getHabitProgressForDate(habitId: Long, date: Long): HabitProgressEntity?
    
    @Query("SELECT * FROM habit_progress WHERE habitId = :habitId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getHabitProgressByDateRange(habitId: Long, startDate: Long, endDate: Long): Flow<List<HabitProgressEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitProgress(progress: HabitProgressEntity): Long
    
    @Update
    suspend fun updateHabitProgress(progress: HabitProgressEntity)
    
    @Query("DELETE FROM habit_progress WHERE habitId = :habitId AND date < :cutoffDate")
    suspend fun deleteOldProgress(habitId: Long, cutoffDate: Long)
    
    @Query("SELECT COUNT(*) FROM habit_progress WHERE isCompleted = 1")
    fun getTotalCompletedHabitsCount(): Flow<Int>
    
    @Query("SELECT SUM(totalCompletions) FROM habits")
    suspend fun getTotalHabitCompletions(): Int?
}
