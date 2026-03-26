package com.neonhabit.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neonhabit.app.data.local.entity.UserProfileEntity
import com.neonhabit.app.data.repository.TaskRepository
import com.neonhabit.app.domain.model.Task
import com.neonhabit.app.domain.model.UserLevel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * UI State для главного экрана
 */
data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val pendingTasksCount: Int = 0,
    val completedTasksCount: Int = 0,
    val userLevel: UserLevel = UserLevel(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val showLevelUpDialog: Boolean = false,
    val searchQuery: String = ""
)

/**
 * ViewModel для главного экрана
 */
class HomeViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadTasks()
        loadUserProfile()
    }
    
    /**
     * Загрузка задач
     */
    private fun loadTasks() {
        viewModelScope.launch {
            taskRepository.pendingTasks
                .catch { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { tasks ->
                    _uiState.update {
                        it.copy(
                            tasks = tasks,
                            pendingTasksCount = tasks.size,
                            isLoading = false
                        )
                    }
                }
        }
        
        viewModelScope.launch {
            taskRepository.completedTasksCount
                .catch { e -> }
                .collect { count ->
                    _uiState.update { it.copy(completedTasksCount = count) }
                }
        }
    }
    
    /**
     * Загрузка профиля пользователя
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            // В реальном приложении загружаем из репозитория
            // Для демо используем дефолтное значение
            _uiState.update { it.copy(userLevel = UserLevel(level = 1, currentExp = 0, requiredExp = 100, totalExp = 0)) }
        }
    }
    
    /**
     * Переключение выполнения задачи
     */
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            try {
                val newCompleted = !task.isCompleted
                taskRepository.updateTaskCompletion(task.id, newCompleted)
                
                if (newCompleted) {
                    // Добавляем опыт
                    addExp(task.expReward)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    
    /**
     * Добавление опыта
     */
    private fun addExp(amount: Int) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val (newLevel, leveledUp) = currentState.userLevel.addExp(amount)
            
            // Обновляем уровень
            _uiState.update { it.copy(userLevel = newLevel, showLevelUpDialog = leveledUp) }
            
            // Сохраняем в БД (в реальном приложении)
            // repository.updateUserProfile(...)
            
            // Скрываем диалог через 3 секунды
            if (leveledUp) {
                kotlinx.coroutines.delay(3000)
                _uiState.update { it.copy(showLevelUpDialog = false) }
            }
        }
    }
    
    /**
     * Удаление задачи
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.deleteTask(task)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    
    /**
     * Поиск задач
     */
    fun searchTasks(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        
        viewModelScope.launch {
            if (query.isEmpty()) {
                loadTasks()
            } else {
                taskRepository.searchTasks(query)
                    .catch { e -> }
                    .collect { tasks ->
                        _uiState.update { it.copy(tasks = tasks) }
                    }
            }
        }
    }
    
    /**
     * Обновление задач по поиску
     */
    fun refreshTasks() {
        loadTasks()
    }
}

/**
 * Factory для создания ViewModel (для использования с ViewModelProvider)
 */
class HomeViewModelFactory(
    private val taskRepository: TaskRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(taskRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
