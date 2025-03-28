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


4. Документация API (Swagger)

После запуска проекта, документация доступна по адресу:

Swagger UI - http://localhost:8080/swagger-ui/index.html

OpenAPI JSON - http://localhost:8080/v3/api-docs


5. Остановка сервиса

docker-compose down


6. Удаление контейнеров и очистка данных

docker-compose down -v


Автор: Osipov Alexey
