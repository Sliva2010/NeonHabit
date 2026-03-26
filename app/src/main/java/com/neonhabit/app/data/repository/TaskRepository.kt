package com.neonhabit.app.data.repository

import com.neonhabit.app.data.local.dao.TaskDao
import com.neonhabit.app.data.local.entity.TaskEntity
import com.neonhabit.app.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Репозиторий для операций с задачами
 */
class TaskRepository(private val taskDao: TaskDao) {
    
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks().map { entities ->
        entities.map { it.toDomainModel() }
    }
    
    val completedTasks: Flow<List<Task>> = taskDao.getTasksByCompletionStatus(true).map { entities ->
        entities.map { it.toDomainModel() }
    }
    
    val pendingTasks: Flow<List<Task>> = taskDao.getTasksByCompletionStatus(false).map { entities ->
        entities.map { it.toDomainModel() }
    }
    
    val completedTasksCount: Flow<Int> = taskDao.getCompletedTasksCount()
    
    val pendingTasksCount: Flow<Int> = taskDao.getPendingTasksCount()
    
    suspend fun getTaskById(id: Long): Task? {
        return taskDao.getTaskById(id)?.toDomainModel()
    }
    
    fun getTasksByCategory(categoryId: Long): Flow<List<Task>> {
        return taskDao.getTasksByCategory(categoryId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    fun searchTasks(query: String): Flow<List<Task>> {
        return taskDao.searchTasks("%$query%").map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    fun getTasksWithDueDate(): Flow<List<Task>> {
        return taskDao.getTasksWithDueDate().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    fun getPrivateTasks(): Flow<List<Task>> {
        return taskDao.getPrivateTasks().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task.toEntity())
    }
    
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }
    
    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }
    
    suspend fun deleteTaskById(id: Long) {
        taskDao.deleteTaskById(id)
    }
    
    suspend fun updateTaskCompletion(id: Long, completed: Boolean) {
        val completedAt = if (completed) System.currentTimeMillis() else null
        taskDao.updateTaskCompletion(
            id = id,
            completed = completed,
            completedAt = completedAt,
            updatedAt = System.currentTimeMillis()
        )
    }
    
    suspend fun getTaskByDateRange(startDate: Long, endDate: Long): List<Task> {
        // Заглушка для будущей реализации
        return emptyList()
    }
}

// Extension functions для маппинга
fun TaskEntity.toDomainModel(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        categoryId = categoryId,
        tagIds = tagIds.split(",").filter { it.isNotEmpty() }.map { it.toLong() },
        dueDate = dueDate,
        reminderTime = reminderTime,
        isCompleted = isCompleted,
        isPrivate = isPrivate,
        completedAt = completedAt,
        expReward = expReward,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        categoryId = categoryId,
        tagIds = tagIds.joinToString(","),
        dueDate = dueDate,
        reminderTime = reminderTime,
        isCompleted = isCompleted,
        isPrivate = isPrivate,
        completedAt = completedAt,
        expReward = expReward,
        note = note,
        createdAt = createdAt,
        updatedAt = System.currentTimeMillis()
    )
}
