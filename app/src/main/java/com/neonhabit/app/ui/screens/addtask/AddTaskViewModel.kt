package com.neonhabit.app.ui.screens.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neonhabit.app.data.repository.TaskRepository
import com.neonhabit.app.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана добавления задачи
 */
class AddTaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddTaskUiState())
    val uiState: StateFlow<AddTaskUiState> = _uiState.asStateFlow()
    
    /**
     * Обновление названия
     */
    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }
    
    /**
     * Обновление описания
     */
    fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }
    
    /**
     * Обновление заметки
     */
    fun updateNote(note: String) {
        _uiState.update { it.copy(note = note) }
    }
    
    /**
     * Выбор категории
     */
    fun selectCategory(categoryId: Long) {
        val currentState = _uiState.value
        _uiState.update {
            it.copy(
                selectedCategoryId = if (currentState.selectedCategoryId == categoryId) null else categoryId
            )
        }
    }
    
    /**
     * Переключение тега
     */
    fun toggleTag(tagId: Long) {
        val currentState = _uiState.value
        val newTags = if (currentState.selectedTagIds.contains(tagId)) {
            currentState.selectedTagIds - tagId
        } else {
            currentState.selectedTagIds + tagId
        }
        _uiState.update { it.copy(selectedTagIds = newTags) }
    }
    
    /**
     * Обновление даты выполнения
     */
    fun updateDueDate(dueDate: Long?) {
        _uiState.update { it.copy(dueDate = dueDate) }
    }
    
    /**
     * Обновление времени напоминания
     */
    fun updateReminderTime(reminderTime: Long?) {
        _uiState.update { it.copy(reminderTime = reminderTime) }
    }
    
    /**
     * Переключение приватности
     */
    fun togglePrivate() {
        _uiState.update { it.copy(isPrivate = !it.isPrivate) }
    }
    
    /**
     * Обновление награды EXP
     */
    fun updateExpReward(exp: Int) {
        _uiState.update { it.copy(expReward = exp) }
    }
    
    /**
     * Сохранение задачи
     */
    fun saveTask() {
        viewModelScope.launch {
            val currentState = _uiState.value
            
            if (currentState.title.isBlank()) {
                _uiState.update { it.copy(error = "Введите название задачи") }
                return@launch
            }
            
            _uiState.update { it.copy(isSaving = true) }
            
            try {
                val task = Task(
                    title = currentState.title.trim(),
                    description = currentState.description.trim(),
                    note = currentState.note.trim(),
                    categoryId = currentState.selectedCategoryId,
                    tagIds = currentState.selectedTagIds,
                    dueDate = currentState.dueDate,
                    reminderTime = currentState.reminderTime,
                    isPrivate = currentState.isPrivate,
                    expReward = currentState.expReward
                )
                
                taskRepository.insertTask(task)
                
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        title = "",
                        description = "",
                        note = "",
                        selectedCategoryId = null,
                        selectedTagIds = emptyList(),
                        dueDate = null,
                        reminderTime = null,
                        isPrivate = false,
                        expReward = 10
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = e.message
                    )
                }
            }
        }
    }
    
    /**
     * Сброс формы
     */
    fun resetForm() {
        _uiState.update {
            AddTaskUiState()
        }
    }
}

/**
 * Factory для создания ViewModel
 */
class AddTaskViewModelFactory(
    private val taskRepository: TaskRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
            return AddTaskViewModel(taskRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
