package com.neonhabit.app.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neonhabit.app.ui.screens.home.HomeScreen
import com.neonhabit.app.ui.screens.addtask.AddTaskScreen
import com.neonhabit.app.ui.screens.habits.HabitsScreen
import com.neonhabit.app.ui.screens.stats.StatsScreen
import com.neonhabit.app.ui.screens.pomodoro.PomodoroScreen
import com.neonhabit.app.ui.screens.inventory.InventoryScreen
import com.neonhabit.app.ui.screens.onboarding.OnboardingScreen

/**
 * Экраны навигации
 */
sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Habits : Screen("habits")
    object Stats : Screen("stats")
    object Pomodoro : Screen("pomodoro")
    object Inventory : Screen("inventory")
    object AddTask : Screen("add_task")
    object AddHabit : Screen("add_habit")
    object Settings : Screen("settings")
}

/**
 * Главный навигационный граф
 */
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    
    // Проверяем, был ли завершен онбординг
    var onboardingCompleted by remember { mutableStateOf(true) } // Для демо всегда true
    
    NavHost(
        navController = navController,
        startDestination = if (onboardingCompleted) Screen.Home.route else Screen.Onboarding.route
    ) {
        // Онбординг
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    onboardingCompleted = true
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Главная
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToHabits = { navController.navigate(Screen.Habits.route) },
                onNavigateToStats = { navController.navigate(Screen.Stats.route) },
                onNavigateToPomodoro = { navController.navigate(Screen.Pomodoro.route) },
                onNavigateToInventory = { navController.navigate(Screen.Inventory.route) },
                onNavigateToAddTask = { navController.navigate(Screen.AddTask.route) }
            )
        }
        
        // Привычки
        composable(Screen.Habits.route) {
            HabitsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddHabit = { navController.navigate(Screen.AddHabit.route) }
            )
        }
        
        // Статистика
        composable(Screen.Stats.route) {
            StatsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Помодоро
        composable(Screen.Pomodoro.route) {
            PomodoroScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Инвентарь
        composable(Screen.Inventory.route) {
            InventoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Добавление задачи
        composable(Screen.AddTask.route) {
            AddTaskScreen(
                onNavigateBack = { navController.popBackStack() },
                onTaskSaved = { navController.popBackStack() }
            )
        }
        
        // Добавление привычки
        composable(Screen.AddHabit.route) {
            // AddHabitScreen будет реализован
        }
    }
}
