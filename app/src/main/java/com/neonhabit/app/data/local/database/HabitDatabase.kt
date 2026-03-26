package com.neonhabit.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.neonhabit.app.data.local.dao.*
import com.neonhabit.app.data.local.entity.*

/**
 * Конвертеры типов для Room
 */
class Converters {
    // Для хранения List<Long> как JSON строки
    @androidx.room.TypeConverter
    fun fromLongList(list: List<Long>): String {
        return list.joinToString(",")
    }
    
    @androidx.room.TypeConverter
    fun toLongList(data: String): List<Long> {
        if (data.isEmpty()) return emptyList()
        return data.split(",").map { it.toLong() }
    }
}

/**
 * Основная база данных приложения
 */
@Database(
    entities = [
        TaskEntity::class,
        HabitEntity::class,
        CategoryEntity::class,
        TagEntity::class,
        HabitProgressEntity::class,
        UserProfileEntity::class,
        RewardEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class HabitDatabase : RoomDatabase() {
    
    abstract fun taskDao(): TaskDao
    abstract fun habitDao(): HabitDao
    abstract fun categoryDao(): CategoryDao
    abstract fun tagDao(): TagDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun rewardDao(): RewardDao
    
    companion object {
        @Volatile
        private var INSTANCE: HabitDatabase? = null
        
        fun getDatabase(context: Context): HabitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "neonhabit_database"
                )
                .fallbackToDestructiveMigration() // Для версии 1.0
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
