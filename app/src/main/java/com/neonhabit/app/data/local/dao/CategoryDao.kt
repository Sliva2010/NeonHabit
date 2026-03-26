package com.neonhabit.app.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.neonhabit.app.data.local.entity.*

/**
 * DAO для операций с категориями и тегами
 */
@Dao
interface CategoryDao {
    
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): CategoryEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long
    
    @Update
    suspend fun updateCategory(category: CategoryEntity)
    
    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
    
    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Long)
}

@Dao
interface TagDao {
    
    @Query("SELECT * FROM tags ORDER BY name ASC")
    fun getAllTags(): Flow<List<TagEntity>>
    
    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getTagById(id: Long): TagEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: TagEntity): Long
    
    @Update
    suspend fun updateTag(tag: TagEntity)
    
    @Delete
    suspend fun deleteTag(tag: TagEntity)
    
    @Query("DELETE FROM tags WHERE id = :id")
    suspend fun deleteTagById(id: Long)
}

@Dao
interface UserProfileDao {
    
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>
    
    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfileEntity)
    
    @Update
    suspend fun updateUserProfile(profile: UserProfileEntity)
    
    @Query("UPDATE user_profile SET level = :level, currentExp = :currentExp, totalExp = :totalExp, updatedAt = :updatedAt WHERE id = 1")
    suspend fun updateLevel(level: Int, currentExp: Int, totalExp: Int, updatedAt: Long)
    
    @Query("UPDATE user_profile SET lives = :lives, updatedAt = :updatedAt WHERE id = 1")
    suspend fun updateLives(lives: Int, updatedAt: Long)
}

@Dao
interface RewardDao {
    
    @Query("SELECT * FROM rewards ORDER BY requiredLevel ASC")
    fun getAllRewards(): Flow<List<RewardEntity>>
    
    @Query("SELECT * FROM rewards WHERE isUnlocked = 1 ORDER BY unlockedAt DESC")
    fun getUnlockedRewards(): Flow<List<RewardEntity>>
    
    @Query("SELECT * FROM rewards WHERE id = :id")
    suspend fun getRewardById(id: Long): RewardEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReward(reward: RewardEntity)
    
    @Update
    suspend fun updateReward(reward: RewardEntity)
    
    @Query("UPDATE rewards SET isUnlocked = 1, unlockedAt = :unlockedAt WHERE id = :id")
    suspend fun unlockReward(id: Long, unlockedAt: Long)
}
