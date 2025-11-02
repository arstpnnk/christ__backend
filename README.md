# Backend — Authentication + Notes API

## Stack
- Spring Boot 2.7.5
- Spring Security + OAuth2 (Google)
- JWT для аутентификации
- PostgreSQL для хранения данных
- REST API: auth, notes CRUD

## Подготовка PostgreSQL
1. Установить и запустить PostgreSQL
2. Создать базу данных:
   ```sql
   psql -U postgres
   CREATE DATABASE backend_db;
   \q
   ```
3. Настроить подключение в `src/main/resources/application.yml`

## Конфигурация
1. В `application.yml` указать:
   - PostgreSQL credentials
   - Google OAuth2 client-id и client-secret
   - JWT secret (минимум 32 байта в base64)
   - Frontend callback URL

## Запуск
```bash
mvn clean package
mvn spring-boot:run
```

## API Endpoints

### Аутентификация
- POST `/auth/register`
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "name": "User Name",
    "phone": "+7999123456"
  }
  ```
  Возвращает JWT при успехе

- POST `/auth/login`
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
  Возвращает JWT при успехе

- GET `/auth/google-url`
  Возвращает URL для начала Google OAuth flow

### Заметки (требуется JWT в заголовке Authorization: Bearer ...)
- POST `/notes`
  ```json
  {
    "title": "Заголовок",
    "body": "Текст заметки"
  }
  ```

- GET `/notes`
  Возвращает список заметок текущего пользователя

- DELETE `/notes/{id}`
  Удаляет заметку по ID (только свои заметки)

## Модели
- User (id, email, password, name, phone, provider)
- Note (id, title, body, user)

## Безопасность
- Все endpoints кроме auth/* требуют JWT
- Пароли хэшируются через BCrypt
- Google OAuth2 для внешней аутентификации