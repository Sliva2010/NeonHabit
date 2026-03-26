# 📐 Архитектура NeonHabit — Реализация 20 функций

## Обзор реализованных функций

| № | Функция | Статус | Файлы реализации |
|---|---------|--------|------------------|
| 1 | Создание, редактирование и удаление ежедневных задач | ✅ | `TaskRepository.kt`, `TaskDao.kt`, `AddTaskScreen.kt`, `HomeScreen.kt` |
| 2 | Создание повторяющихся привычек | ✅ | `HabitRepository.kt`, `HabitDao.kt`, `HabitCard.kt` |
| 3 | Система уровней пользователя (EXP) | ✅ | `UserLevel.kt`, `HomeViewModel.kt` |
| 4 | Анимированный прогресс-бар уровня | ✅ | `NeonProgressBar.kt`, `LevelProgressBar` |
| 5 | Инвентарь наград | ✅ | `RewardEntity.kt`, `InventoryScreen.kt` |
| 6 | Строгий темный режим | ✅ | `Theme.kt`, `Color.kt` |
| 7 | Экран детальной статистики | 🟡 | `StatsScreen.kt` (заглушка) |
| 8 | Помодоро таймер | 🟡 | `PomodoroScreen.kt` (заглушка) |
| 9 | Теги и категории | ✅ | `CategoryEntity.kt`, `TagEntity.kt`, `AddTaskScreen.kt` |
| 10 | Календарь-виджет | 🟡 | Требуется реализация |
| 11 | Биометрический вход | 🟡 | Требуется реализация `BiometricManager.kt` |
| 12 | Drag-and-drop сортировка | 🟡 | Требуется реализация |
| 13 | Система "Жизней" | ✅ | `HabitEntity.kt`, `LivesIndicator.kt` |
| 14 | Уведомления-напоминания | 🟡 | `NotificationManager.kt` (требуется) |
| 15 | Поиск по задачам | ✅ | `TaskDao.searchTasks()`, `HomeScreen.kt` |
| 16 | Заметки с Markdown | ✅ | `TaskEntity.note`, `NoteSection` |
| 17 | Заморозка привычки | ✅ | `HabitEntity.isFrozen`, `HabitRepository.updateFreezeStatus()` |
| 18 | Резервное копирование в JSON | 🟡 | Требуется `BackupManager.kt` |
| 19 | Звуковые эффекты | 🟡 | Требуется `SoundManager.kt` |
| 20 | Интерактивный туториал | ✅ | `OnboardingScreen.kt` |

**Условные обозначения:**
- ✅ Полностью реализовано
- 🟡 Частично реализовано / заглушка
- 🔴 Не реализовано

---

## Детальная архитектура по функциям

### 1. Создание, редактирование и удаление ежедневных задач

**Реализация:**
```
┌─────────────────────────────────────────────────────────┐
│                    UI Layer (Compose)                   │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │ AddTaskScreen│  │ HomeScreen  │  │ TaskCard    │     │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘     │
│         │                │                │             │
│         ▼                ▼                ▼             │
│  ┌─────────────────────────────────────────────────┐   │
│  │           AddTaskViewModel / HomeViewModel      │   │
│  └─────────────────────┬───────────────────────────┘   │
└────────────────────────┼───────────────────────────────┘
                         │
┌────────────────────────▼───────────────────────────────┐
│                    Repository Layer                     │
│  ┌─────────────────────────────────────────────────┐   │
│  │              TaskRepository                     │   │
│  │  - insertTask()                                 │   │
│  │  - updateTask()                                 │   │
│  │  - deleteTask()                                 │   │
│  │  - updateTaskCompletion()                       │   │
│  └─────────────────────┬───────────────────────────┘   │
└────────────────────────┼───────────────────────────────┘
                         │
┌────────────────────────▼───────────────────────────────┐
│                    Data Layer (Room)                    │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │  TaskDao    │  │ TaskEntity  │  │ Converters  │     │
│  │  - insert() │  │  - id       │  │  - List<Long│     │
│  │  - update() │  │  - title    │  │    → String │     │
│  │  - delete() │  │  - isPrivate│  │             │     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
└─────────────────────────────────────────────────────────┘
```

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/data/local/entity/TaskEntity.kt`
- `app/src/main/java/com/neonhabit/app/data/local/dao/TaskDao.kt`
- `app/src/main/java/com/neonhabit/app/data/repository/TaskRepository.kt`
- `app/src/main/java/com/neonhabit/app/ui/screens/addtask/AddTaskScreen.kt`
- `app/src/main/java/com/neonhabit/app/ui/screens/home/HomeScreen.kt`

---

### 2. Создание повторяющихся привычек

**Модель данных:**
```kotlin
data class Habit(
    val id: Long = 0,
    val title: String,
    val frequency: HabitFrequency, // DAILY, WEEKLY, CUSTOM
    val targetCount: Int = 1,      // Сколько раз в день
    val scheduledTime: Long?,      // Время напоминания
    val streak: Int = 0,           // Текущая серия
    val bestStreak: Int = 0,       // Лучшая серия
    val totalCompletions: Int = 0, // Всего выполнений
    val lives: Int = 3             // Жизни
)
```

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/domain/model/Habit.kt`
- `app/src/main/java/com/neonhabit/app/data/local/entity/HabitEntity.kt`
- `app/src/main/java/com/neonhabit/app/data/repository/HabitRepository.kt`
- `app/src/main/java/com/neonhabit/app/ui/components/HabitCard.kt`

---

### 3. Система уровней пользователя (EXP)

**Алгоритм начисления опыта:**
```kotlin
data class UserLevel(
    val level: Int = 1,
    val currentExp: Int = 0,
    val requiredExp: Int = 100,
    val totalExp: Int = 0
) {
    fun addExp(amount: Int): Pair<UserLevel, Boolean> {
        var newExp = currentExp + amount
        var newLevel = level
        var newRequiredExp = requiredExp
        var leveledUp = false
        
        while (newExp >= newRequiredExp) {
            newExp -= newRequiredExp
            newLevel++
            newRequiredExp = calculateRequiredExp(newLevel)
            leveledUp = true
        }
        
        return Pair(UserLevel(newLevel, newExp, newRequiredExp, totalExp + amount), leveledUp)
    }
    
    companion object {
        fun calculateRequiredExp(level: Int): Int {
            return (100 * kotlin.math.pow(level.toDouble(), 1.5)).toInt()
        }
    }
}
```

**Таблица уровней:**
| Уровень | Требуется EXP | Всего EXP |
|---------|---------------|-----------|
| 1 → 2   | 100           | 100       |
| 2 → 3   | 283           | 383       |
| 3 → 4   | 520           | 903       |
| 4 → 5   | 800           | 1703      |
| 5 → 10  | ~2000         | ~10000    |
| 10 → 20 | ~5000         | ~50000    |

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/domain/model/UserLevel.kt`
- `app/src/main/java/com/neonhabit/app/ui/screens/home/HomeViewModel.kt`

---

### 4. Анимированный прогресс-бар уровня

**Реализация анимации:**
```kotlin
@Composable
fun NeonProgressBar(
    progress: Float,
    colors: List<Color> = listOf(NeonPink, NeonPurple, NeonCyan)
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )
    
    // Слой свечения
    Box(
        modifier = Modifier
            .blur(15.dp)
            .background(
                Brush.horizontalGradient(
                    colors.map { it.copy(alpha = 0.4f) }
                )
            )
    )
    
    // Основной прогресс
    Box(
        modifier = Modifier
            .fillMaxWidth(animatedProgress)
            .background(Brush.horizontalGradient(colors))
    )
}
```

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/ui/components/NeonProgressBar.kt`
- `app/src/main/java/com/neonhabit/app/ui/components/NeonButton.kt`

---

### 5. Инвентарь наград

**Модель награды:**
```kotlin
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
    val rarity: RewardRarity // COMMON, RARE, EPIC, LEGENDARY
)
```

**Награды по умолчанию:**
| ID | Название | Описание | Требование | Редкость |
|----|----------|----------|------------|----------|
| 1 | Первый шаг | Выполните первую задачу | 1 completion | COMMON |
| 2 | Новичок | Достигните 5 уровня | Level 5 | COMMON |
| 3 | Серийный убийца | Серия из 7 дней | 7 streak | RARE |
| 4 | Мастер | Достигните 10 уровня | Level 10 | EPIC |
| 5 | Легенда | Достигните 20 уровня | Level 20 | LEGENDARY |

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/domain/model/UserLevel.kt`
- `app/src/main/java/com/neonhabit/app/data/local/entity/CategoryEntity.kt` (RewardEntity)
- `app/src/main/java/com/neonhabit/app/NeonHabitApp.kt` (инициализация)

---

### 6. Строгий темный режим (AMOLED Black)

**Цветовая палитра:**
```kotlin
// Основные цвета
val NeonBlack = Color(0xFF000000)      // Полностью черный для AMOLED
val NeonDarkGray = Color(0xFF0A0A0A)   // Карточки
val NeonMediumGray = Color(0xFF1A1A1A) // Элементы 2 уровня
val NeonLightGray = Color(0xFF2A2A2A)  // Границы

// Неоновые акценты
val NeonPink = Color(0xFFFF00FF)
val NeonCyan = Color(0xFF00FFFF)
val NeonPurple = Color(0xFF8000FF)
val NeonGreen = Color(0xFF00FF00)
val NeonOrange = Color(0xFFFF8000)
val NeonRed = Color(0xFFFF0040)
```

**Принудительная темная тема:**
```kotlin
@Composable
fun NeonHabitTheme(
    darkTheme: Boolean = true, // Всегда true
    dynamicColor: Boolean = false, // Отключено для сохранения палитры
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    
    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = NeonBlack.toArgb()
        window.navigationBarColor = NeonBlack.toArgb()
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
    }
    
    MaterialTheme(colorScheme = colorScheme, content = content)
}
```

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/ui/theme/Color.kt`
- `app/src/main/java/com/neonhabit/app/ui/theme/Theme.kt`

---

### 7. Экран детальной статистики

**Требуемая реализация:**
- Графики выполнения по дням (MPAndroidChart или Compose Canvas)
- Статистика по категориям
- Прогресс за неделю/месяц
- Лучшие привычки

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/ui/screens/stats/StatsScreen.kt` (заглушка)

---

### 8. Помодоро таймер

**Требуемая реализация:**
```kotlin
data class PomodoroState(
    val isRunning: Boolean = false,
    val currentTime: Int = POMODORO_FOCUS_MINUTES * 60,
    val sessionType: SessionType = SessionType.FOCUS,
    val completedSessions: Int = 0
)

enum class SessionType {
    FOCUS,      // 25 минут
    SHORT_BREAK, // 5 минут
    LONG_BREAK   // 15 минут
}
```

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/ui/screens/pomodoro/PomodoroScreen.kt` (заглушка)

---

### 9. Теги и категории

**Модели:**
```kotlin
data class Category(
    val id: Long,
    val name: String,
    val color: Long, // ARGB
    val icon: String
)

data class Tag(
    val id: Long,
    val name: String,
    val color: Long
)
```

**Категории по умолчанию:**
- Работа (NeonPink)
- Здоровье (NeonGreen)
- Обучение (NeonCyan)
- Хобби (NeonPurple)
- Дом (NeonOrange)
- Финансы (NeonYellow)

**Теги по умолчанию:**
- Срочно (NeonRed)
- Важно (NeonOrange)
- Быстро (NeonGreen)
- Долгосрок (NeonBlue)

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/data/local/entity/CategoryEntity.kt`
- `app/src/main/java/com/neonhabit/app/ui/screens/addtask/AddTaskScreen.kt`

---

### 10. Календарь-виджет

**Требуемая реализация:**
- Отображение месяца с отметками выполненных задач
- Heatmap активности (как GitHub contributions)
- Фильтрация по привычкам

---

### 11. Биометрический вход

**Требуемая реализация:**
```kotlin
class BiometricManager(private val context: Context) {
    fun authenticate(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val biometricPrompt = BiometricPrompt(...)
        // Android Biometric API
    }
}
```

**Разрешения в AndroidManifest.xml:**
```xml
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
```

---

### 12. Drag-and-drop сортировка

**Требуемая реализация:**
```kotlin
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReorderableTaskList(tasks: List<Task>) {
    val state = rememberReorderableLazyListState { from, to ->
        // Перемещение элементов
    }
    
    LazyColumn(state = state.listState) {
        items(tasks) { task ->
            ReorderableItem(state, key = task.id) {
                TaskCard(task, ...)
            }
        }
    }
}
```

---

### 13. Система "Жизней"

**Реализация:**
```kotlin
data class Habit(
    val lives: Int = 3,  // Максимум 3 жизни
    val streak: Int = 0
)

// В HabitRepository:
suspend fun decrementLife(id: Long) {
    habitDao.decrementLife(id, System.currentTimeMillis())
}

// Проверка пропущенных дней
suspend fun checkMissedHabits() {
    habits.forEach { habit ->
        if (!completedToday(habit.id)) {
            decrementLife(habit.id)
            if (habit.lives <= 0) {
                resetStreak(habit.id)
            }
        }
    }
}
```

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/ui/components/NeonProgressBar.kt` (LivesIndicator)
- `app/src/main/java/com/neonhabit/app/data/local/dao/HabitDao.kt`

---

### 14. Уведомления-напоминания

**Требуемая реализация:**
```kotlin
class NotificationManager(private val context: Context) {
    fun scheduleReminder(task: Task) {
        // WorkManager для отложенных уведомлений
    }
    
    fun showNotification(title: String, message: String) {
        // Показ уведомления
    }
}
```

**Разрешения:**
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
```

---

### 15. Поиск по задачам

**Реализация:**
```kotlin
// В TaskDao:
@Query("SELECT * FROM tasks WHERE title LIKE :query OR description LIKE :query ORDER BY createdAt DESC")
fun searchTasks(query: String): Flow<List<TaskEntity>>

// В HomeScreen:
val searchQuery by viewModel.uiState.collectAsState().searchQuery
SearchBar(
    query = searchQuery,
    onQueryChange = viewModel::searchTasks
)
```

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/data/local/dao/TaskDao.kt`
- `app/src/main/java/com/neonhabit/app/ui/screens/home/HomeScreen.kt`

---

### 16. Заметки с Markdown

**Реализация:**
```kotlin
data class Task(
    val note: String = "" // Markdown текст
)

// Для рендеринга используйте библиотеку:
// implementation("io.noties.markwon:core:4.6.2")

@Composable
fun MarkdownText(markdown: String) {
    val markdownRender = remember { Markwon.create(context) }
    AndroidView(
        factory = { TextView(it) },
        update = { markdownRender.setMarkdown(it, markdown) }
    )
}
```

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/data/local/entity/TaskEntity.kt`
- `app/src/main/java/com/neonhabit/app/ui/screens/addtask/AddTaskScreen.kt` (NoteSection)

---

### 17. Заморозка привычки

**Реализация:**
```kotlin
data class Habit(
    val isFrozen: Boolean = false,
    val frozenUntil: Long? = null // Timestamp
)

// В HabitRepository:
suspend fun updateFreezeStatus(id: Long, frozen: Boolean, frozenUntil: Long?) {
    habitDao.updateFreezeStatus(id, frozen, frozenUntil, System.currentTimeMillis())
}

// Проверка при загрузке:
suspend fun checkFrozenHabits() {
    val now = System.currentTimeMillis()
    habits.filter { 
        it.isFrozen && it.frozenUntil != null && it.frozenUntil!! < now 
    }.forEach {
        updateFreezeStatus(it.id, false, null)
    }
}
```

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/data/local/entity/HabitEntity.kt`
- `app/src/main/java/com/neonhabit/app/data/repository/HabitRepository.kt`

---

### 18. Резервное копирование в JSON

**Требуемая реализация:**
```kotlin
class BackupManager(private val context: Context) {
    suspend fun exportData(): String {
        val data = BackupData(
            tasks = taskRepository.allTasks.first(),
            habits = habitRepository.allHabits.first(),
            userLevel = userProfile,
            categories = categories,
            tags = tags
        )
        return Json.encodeToString(BackupData.serializer(), data)
    }
    
    suspend fun importData(json: String) {
        val data = Json.decodeFromString(BackupData.serializer(), json)
        // Восстановление данных
    }
}

@Serializable
data class BackupData(
    val tasks: List<Task>,
    val habits: List<Habit>,
    val userLevel: UserLevel,
    val categories: List<Category>,
    val tags: List<Tag>
)
```

---

### 19. Звуковые эффекты

**Требуемая реализация:**
```kotlin
class SoundManager(private val context: Context) {
    private val soundPool = SoundPool.Builder().build()
    private val sounds = mutableMapOf<String, Int>()
    
    init {
        sounds["click"] = soundPool.load(context, R.raw.click_sound, 1)
        sounds["success"] = soundPool.load(context, R.raw.success_sound, 1)
        sounds["levelup"] = soundPool.load(context, R.raw.levelup_sound, 1)
    }
    
    fun play(sound: String) {
        soundPool.play(sounds[sound]!!, 1f, 1f, 1, 0, 1f)
    }
}
```

**Файлы звуков:**
- `app/src/main/res/raw/click_sound.mp3`
- `app/src/main/res/raw/success_sound.mp3`
- `app/src/main/res/raw/levelup_sound.mp3`

---

### 20. Интерактивный туториал (Onboarding)

**Реализация:**
```kotlin
@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    
    val pages = listOf(
        OnboardingPage("🚀", "Добро пожаловать", "Твой трекер привычек"),
        OnboardingPage("📋", "Создавай привычки", "Ежедневные задачи"),
        OnboardingPage("🏆", "Получай награды", "Открывай достижения")
    )
    
    HorizontalPager(state = pagerState) { page ->
        OnboardingPageContent(pages[page])
    }
    
    // Индикаторы страниц
    // Кнопки "Далее" / "Начать"
}
```

**Ключевые файлы:**
- `app/src/main/java/com/neonhabit/app/ui/screens/onboarding/OnboardingScreen.kt`

---

## 📁 Полная структура проекта

```
NeonHabit/
├── .github/workflows/build.yml          # CI/CD
├── .gitignore
├── README.md
├── GIT_SETUP_INSTRUCTION.md
├── ARCHITECTURE.md                       # Этот файл
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew.bat
├── gradle/wrapper/gradle-wrapper.properties
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/neonhabit/app/
│       │   ├── NeonHabitApp.kt
│       │   ├── MainActivity.kt
│       │   ├── data/
│       │   │   ├── local/
│       │   │   │   ├── database/
│       │   │   │   │   └── HabitDatabase.kt
│       │   │   │   └── dao/
│       │   │   │       ├── TaskDao.kt
│       │   │   │       ├── HabitDao.kt
│       │   │   │       └── CategoryDao.kt
│       │   │   ├── entity/
│       │   │   │   ├── TaskEntity.kt
│       │   │   │   ├── HabitEntity.kt
│       │   │   │   └── CategoryEntity.kt
│       │   │   └── repository/
│       │   │       ├── TaskRepository.kt
│       │   │       └── HabitRepository.kt
│       │   ├── domain/
│       │   │   ├── model/
│       │   │   │   ├── Task.kt
│       │   │   │   ├── Habit.kt
│       │   │   │   ├── UserLevel.kt
│       │   │   │   └── Reward.kt
│       │   │   └── util/
│       │   │       └── Constants.kt
│       │   ├── ui/
│       │   │   ├── theme/
│       │   │   │   ├── Color.kt
│       │   │   │   ├── Theme.kt
│       │   │   │   └── Type.kt
│       │   │   ├── components/
│       │   │   │   ├── NeonButton.kt
│       │   │   │   ├── NeonProgressBar.kt
│       │   │   │   ├── TaskCard.kt
│       │   │   │   └── HabitCard.kt
│       │   │   ├── screens/
│       │   │   │   ├── home/
│       │   │   │   │   ├── HomeScreen.kt
│       │   │   │   │   └── HomeViewModel.kt
│       │   │   │   ├── addtask/
│       │   │   │   │   ├── AddTaskScreen.kt
│       │   │   │   │   └── AddTaskViewModel.kt
│       │   │   │   ├── habits/
│       │   │   │   │   └── HabitsScreen.kt
│       │   │   │   ├── stats/
│       │   │   │   │   └── StatsScreen.kt
│       │   │   │   ├── pomodoro/
│       │   │   │   │   └── PomodoroScreen.kt
│       │   │   │   ├── inventory/
│       │   │   │   │   └── InventoryScreen.kt
│       │   │   │   └── onboarding/
│       │   │   │       └── OnboardingScreen.kt
│       │   │   └── navigation/
│       │   │       └── NavGraph.kt
│       │   └── util/
│       │       ├── BiometricManager.kt (требуется)
│       │       ├── NotificationManager.kt (требуется)
│       │       └── BackupManager.kt (требуется)
│       └── res/
│           ├── values/
│           │   ├── colors.xml
│           │   ├── strings.xml
│           │   └── themes.xml
│           └── drawable/
└── local.properties (не коммитить!)
```

---

## 🚀 Следующие шаги

1. **Открыть проект в Android Studio**
2. **Синхронизировать Gradle**
3. **Запустить сборку:** `./gradlew assembleDebug`
4. **Установить на устройство/эмулятор**
5. **Доработать заглушки** (статистика, помодоро, биометрия)

---

**Документ создан для NeonHabit v1.0**
