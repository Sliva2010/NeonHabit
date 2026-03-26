# 🚀 NeonHabit

**Геймифицированный трекер привычек с неоновым стилем**

![Android](https://img.shields.io/badge/Android-10+-green.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-purple.svg)
![Compose](https://img.shields.io/badge/Compose-1.5-blue.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

## 📱 Описание

NeonHabit — это современное Android-приложение для отслеживания привычек и задач с глубоким темным (AMOLED) стилем и яркими неоновыми акцентами. Приложение использует геймификацию для мотивации пользователей: выполняйте задачи, получайте опыт, повышайте уровень и открывайте награды!

## ✨ Особенности

### 🎯 Основные функции

1. **Управление задачами** — создание, редактирование, удаление ежедневных задач
2. **Привычки** — повторяющиеся привычки с настраиваемой частотой
3. **Система уровней** — получайте EXP за выполненные задачи
4. **Анимированный прогресс** — красивые прогресс-бары уровня
5. **Инвентарь наград** — открывайте достижения
6. **Строгий темный режим** — AMOLED black с неоновыми акцентами
7. **Статистика** — графики выполнения по дням
8. **Помодоро таймер** — круговая анимация для фокусировки
9. **Теги и категории** — организация с цветами
10. **Календарь** — просмотр истории выполнений
11. **Биометрия** — защита приватных задач
12. **Drag-and-drop** — сортировка задач с анимацией
13. **Система жизней** — мотивация не пропускать привычки
14. **Уведомления** — напоминания о задачах
15. **Поиск** — быстрый поиск по задачам
16. **Markdown заметки** — заметки к задачам
17. **Заморозка привычек** — пауза на время отпуска
18. **Экспорт данных** — резервное копирование в JSON
19. **Звуковые эффекты** — тактильная отдача
20. **Онбординг** — интерактивный туториал

## 🛠 Технологический стек

- **Kotlin** 1.9.22
- **Jetpack Compose** — современный declarative UI
- **Material 3** — компоненты дизайна
- **Room** — локальная база данных
- **ViewModel + StateFlow** — архитектура MVVM
- **Navigation Compose** — навигация
- **DataStore** — хранение настроек
- **Biometric** — биометрическая аутентификация
- **WorkManager** — фоновые задачи
- **Kotlin Serialization** — JSON экспорт/импорт
- **Lottie** — сложные анимации
- **GitHub Actions** — CI/CD

## 🏗 Архитектура

Приложение следует архитектуре **MVVM** с четким разделением на слои:

```
app/
├── data/           # Слой данных
│   ├── local/      # Локальные источники (Room, DataStore)
│   └── repository/ # Репозитории
├── domain/         # Бизнес-логика
│   ├── model/      # Модели предметной области
│   └── util/       # Утилиты
└── ui/             # UI слой
    ├── theme/      # Тема, цвета, типографика
    ├── components/ # Переиспользуемые компоненты
    ├── screens/    # Экраны приложения
    └── navigation/ # Навигация
```

## 🚀 Быстрый старт

### Требования

- Android Studio Hedgehog (2023.1.1) или новее
- JDK 17
- Android SDK 34
- Эмулятор или устройство с Android 10+

### Установка

1. **Клонируйте репозиторий**
```bash
git clone https://github.com/yourusername/neonhabit.git
cd neonhabit
```

2. **Откройте в Android Studio**
   - File → Open → выберите папку проекта
   - Дождитесь синхронизации Gradle

3. **Запустите приложение**
   - Нажмите ▶ Run или `Shift+F10`
   - Выберите эмулятор или устройство

### Сборка APK через командную строку

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

APK файлы будут в:
- `app/build/outputs/apk/debug/`
- `app/build/outputs/apk/release/`

## 📦 Структура проекта

```
NeonHabit/
├── .github/workflows/    # GitHub Actions CI/CD
├── app/
│   ├── src/main/
│   │   ├── java/com/neonhabit/app/
│   │   │   ├── data/          # Слой данных
│   │   │   ├── domain/        # Бизнес-логика
│   │   │   ├── ui/            # UI компоненты
│   │   │   ├── util/          # Утилиты
│   │   │   └── *.kt           # Application, MainActivity
│   │   ├── res/               # Ресурсы Android
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 🎨 Дизайн-система

### Цветовая палитра

| Цвет | Hex | Использование |
|------|-----|---------------|
| Neon Black | `#000000` | Фон (AMOLED) |
| Neon Pink | `#FF00FF` | Основной акцент |
| Neon Cyan | `#00FFFF` | Вторичный акцент |
| Neon Purple | `#8000FF` | Третичный акцент |
| Neon Green | `#00FF00` | Успех |
| Neon Red | `#FF0040` | Ошибка, жизни |

### Анимации

- **Spring animations** — естественные физические анимации
- **Glow effects** — неоновое свечение
- **Confetti** — празднование достижений
- **Scale & Fade** — плавные переходы

## 🔐 Безопасность

- Биометрическая защита для приватных задач
- Локальное хранение данных (Room)
- Экспорт/импорт данных в зашифрованный JSON

## 📄 Лицензия

MIT License — см. [LICENSE](LICENSE) файл

## 🤝 Вклад

Pull Request приветствуются! Пожалуйста:

1. Fork репозиторий
2. Создайте feature branch (`git checkout -b feature/amazing-feature`)
3. Commit изменения (`git commit -m 'Add amazing feature'`)
4. Push в branch (`git push origin feature/amazing-feature`)
5. Откройте Pull Request

## 📞 Контакты

- **Email**: your.email@example.com
- **Telegram**: @yourusername
- **GitHub**: Issues

## 🙏 Благодарности

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Room Database](https://developer.android.com/training/data-storage/room)

---

**Made with ❤️ and ☕ by Your Name**

⭐ Если вам нравится проект, поставьте звезду!
