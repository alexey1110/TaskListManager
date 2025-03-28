Task List Manager


1. Описание проекта

Task List Manager — это API-сервис для управления задачами. Поддерживает CRUD-операции для задач, управление приоритетами, статусами и назначение исполнителей.

2. Требования

Перед началом работы убедитесь, что у вас установлены:

- Docker

- Docker Compose

- Java 17+

- Maven 3+

3. Установка и запуск

- Клонирование репозитория

git clone https://github.com/username/taskListManager.git
cd taskListManager

- Сборка проекта

mvn clean package

- Запуск через Docker Compose

docker-compose up --build

После успешного запуска API будет доступно по адресу: http://localhost:8080

4. Переменные окружения

SPRING_DATASOURCE_URL - URL базы данных (по умолчанию PostgreSQL jdbc:postgresql://tasklist_db:5432/taskdb)

SPRING_DATASOURCE_USERNAME - Имя пользователя БД (по умолчанию postgres)

SPRING_DATASOURCE_PASSWORD - Пароль пользователя БД (по умолчанию postgres)

JWT_SECRET - Секретный ключ для JWT-токенов
 
5. Документация API (Swagger)

После запуска проекта, документация доступна по адресу:

Swagger UI - http://localhost:8080/swagger-ui/index.html

OpenAPI JSON - http://localhost:8080/v3/api-docs

6. Остановка сервиса

docker-compose down

7. Удаление контейнеров и очистка данных

docker-compose down -v

Автор: Osipov Alexey
