# 📋 Пошаговая инструкция: Git Init и Push проекта NeonHabit

Эта инструкция поможет вам инициализировать локальный Git-репозиторий и отправить проект на GitHub через командную строку, чтобы обойти лимит в 100 файлов для веб-интерфейса.

---

## 📌 Предварительные требования

1. **Установлен Git**
   - Скачайте с https://git-scm.com/
   - Установите с настройками по умолчанию

2. **Аккаунт на GitHub**
   - Если нет, зарегистрируйтесь на https://github.com

3. **Установлен Android Studio** (для открытия проекта после клонирования)

---

## 🔧 Шаг 1: Инициализация Git-репозитория

Откройте **PowerShell** или **Command Prompt** в папке с проектом:

```bash
cd C:\Users\slava\AppData\Local\Programs\Microsoft VS Code\NeonHabit
```

Или кликните правой кнопкой мыши в папке проекта → "Open in Terminal" (в VS Code) или "Git Bash Here" (если установлен Git).

### Инициализируйте репозиторий:

```bash
git init
```

Вы увидите сообщение:
```
Initialized empty Git repository in C:/Users/slava/AppData/Local/Programs/Microsoft VS Code/NeonHabit/.git/
```

---

## 🔧 Шаг 2: Настройка пользователя Git

Если вы ещё не настраивали Git, выполните:

```bash
git config --global user.name "Ваше Имя"
git config --global user.email "your.email@example.com"
```

---

## 🔧 Шаг 3: Проверка файлов

Посмотрите, какие файлы Git видит:

```bash
git status
```

Вы увидите список файлов в красном цвете (не отслеживаются).

---

## 🔧 Шаг 4: Добавление всех файлов

Добавьте все файлы в индекс Git:

```bash
git add .
```

Или добавьте конкретные файлы:

```bash
git add app/src/main/java/com/neonhabit/app/
git add app/build.gradle.kts
git add build.gradle.kts
# и т.д.
```

Проверьте статус снова:

```bash
git status
```

Теперь файлы должны быть зелёными (готовы к коммиту).

---

## 🔧 Шаг 5: Первый коммит

Создайте первый коммит:

```bash
git commit -m "Initial commit: NeonHabit v1.0 - Геймифицированный трекер привычек"
```

---

## 🔧 Шаг 6: Создание репозитория на GitHub

### Вариант A: Через веб-интерфейс (рекомендуется)

1. Откройте https://github.com/new
2. Введите имя репозитория: `NeonHabit`
3. Описание (опционально): "Геймифицированный трекер привычек с неоновым стилем"
4. **НЕ** ставьте галочки на "Initialize this repository with a README"
5. Нажмите **Create repository**

### Вариант B: Через GitHub CLI (если установлен)

```bash
gh repo create NeonHabit --public --source=. --remote=origin --push
```

---

## 🔧 Шаг 7: Привязка удалённого репозитория

После создания репозитория на GitHub, привяжите его к локальному:

```bash
git remote add origin https://github.com/ВАШ_ЮЗЕРНЕЙМ/NeonHabit.git
```

Проверьте подключение:

```bash
git remote -v
```

Должно вывести:
```
origin  https://github.com/ВАШ_ЮЗЕРНЕЙМ/NeonHabit.git (fetch)
origin  https://github.com/ВАШ_ЮЗЕРНЕЙМ/NeonHabit.git (push)
```

---

## 🔧 Шаг 8: Отправка проекта на GitHub

### Переименуйте ветку в main (если нужно):

```bash
git branch -M main
```

### Отправьте файлы:

```bash
git push -u origin main
```

Если используется двухфакторная аутентификация, GitHub запросит не пароль, а **Personal Access Token**.

---

## 🔐 Создание Personal Access Token (если требуется)

1. Откройте https://github.com/settings/tokens
2. Нажмите **Generate new token (classic)**
3. Дайте название: `NeonHabit CLI`
4. Выберите срок действия (рекомендуется 90 дней)
5. Отметьте права:
   - ✅ `repo` (полный доступ к репозиториям)
6. Нажмите **Generate token**
7. **Скопируйте токен** (показать его можно будет только один раз!)

При пуше используйте токен вместо пароля.

---

## 🔧 Шаг 9: Проверка на GitHub

1. Откройте https://github.com/ВАШ_ЮЗЕРНЕЙМ/NeonHabit
2. Вы должны увидеть все файлы проекта
3. Проверьте, что файлы загрузились корректно

---

## 🚀 Шаг 10: Настройка GitHub Actions

После пуша workflow файл автоматически активируется.

### Проверка статуса сборки:

1. Перейдите на вкладку **Actions** в вашем репозитории
2. Вы увидите запущенный workflow "Android CI/CD"
3. Дождитесь завершения сборки (15-30 минут)
4. После завершения скачайте APK из раздела **Artifacts**

---

## 📥 Скачать APK из GitHub Actions

1. Откройте https://github.com/ВАШ_ЮЗЕРНЕЙМ/NeonHabit/actions
2. Кликните на последний запуск (с зелёной галочкой ✅)
3. Прокрутите вниз до раздела **Artifacts**
4. Нажмите на `NeonHabit-release-v*` или `NeonHabit-debug-v*`
5. APK файл загрузится на ваш компьютер

---

## 🔁 Последующие изменения

### Для отправки изменений:

```bash
# 1. Проверка изменений
git status

# 2. Добавление изменённых файлов
git add .

# 3. Коммит
git commit -m "Описание изменений"

# 4. Отправка
git push origin main
```

---

## ⚠️ Возможные проблемы и решения

### Ошибка: "fatal: remote origin already exists"

```bash
git remote remove origin
git remote add origin https://github.com/ВАШ_ЮЗЕРНЕЙМ/NeonHabit.git
```

### Ошибка: "failed to push some refs"

```bash
git pull origin main --rebase
git push origin main
```

### Ошибка: "Large files detected"

Если файлы больше 100MB, используйте Git LFS:

```bash
git lfs install
git lfs track "*.apk"
git lfs track "*.aar"
git add .gitattributes
git add .
git commit -m "Add LFS tracking"
git push origin main
```

### Ошибка аутентификации

Используйте Personal Access Token вместо пароля (см. выше).

---

## 📊 Структура отправленных файлов

После успешного пуша на GitHub будут отправлены:

```
NeonHabit/
├── .github/workflows/build.yml    # CI/CD конфигурация
├── .gitignore                     # Игнорируемые файлы
├── README.md                      # Документация
├── build.gradle.kts               # Корневой Gradle
├── settings.gradle.kts            # Настройки проекта
├── gradle.properties              # Свойства Gradle
├── gradlew.bat                    # Gradle wrapper (Windows)
├── gradle/wrapper/
│   └── gradle-wrapper.properties
├── app/
│   ├── build.gradle.kts           # App-level Gradle
│   ├── proguard-rules.pro         # ProGuard правила
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/neonhabit/app/
│       │   ├── NeonHabitApp.kt
│       │   ├── MainActivity.kt
│       │   ├── data/              # Слой данных
│       │   ├── domain/            # Бизнес-логика
│       │   └── ui/                # UI слой
│       └── res/                   # Ресурсы
└── local.properties               # Локальные настройки (не коммитить!)
```

---

## ✅ Чек-лист успешной отправки

- [ ] Git репозиторий инициализирован
- [ ] Все файлы добавлены в коммит
- [ ] Репозиторий создан на GitHub
- [ ] Remote origin настроен
- [ ] Push выполнен успешно
- [ ] Файлы видны на GitHub
- [ ] GitHub Actions запустился
- [ ] APK собран и доступен для скачивания

---

## 📞 Если что-то пошло не так

1. Проверьте логи Git:
   ```bash
   git log --oneline
   ```

2. Проверьте статус:
   ```bash
   git status
   ```

3. Отмените последний коммит (если нужно):
   ```bash
   git reset --soft HEAD~1
   ```

4. Создайте issue на GitHub с описанием проблемы.

---

**Удачи с разработкой NeonHabit! 🚀**
