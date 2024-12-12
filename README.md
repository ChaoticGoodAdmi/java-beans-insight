# Beans Insight

## Описание

**Beans Insight** – это микросервис аналитики для сети кофеен. Сервис обрабатывает события создания заказов и предоставляет статистику, такую как объем продаж, выручка за период, средний чек и топ популярных товаров. Аналитика помогает владельцам кофеен принимать более обоснованные решения для увеличения продаж и улучшения обслуживания клиентов.

## Ключевые функции

- Подсчет общего количества заказов кофейни.
- Расчет среднего чека за указанный период.
- Вычисление общей выручки за последний месяц.
- Определение топ-10 популярных товаров кофейни.
- Асинхронная обработка событий заказов через Kafka.

## Требования по инфраструктуре

1. **База данных:** ClickHouse
    - Доступ через сервис Kubernetes: `insight-clickhouse-svc:8123`
    - Требуется предварительное создание базы данных (`insight_db`) и таблиц.

2. **Очередь сообщений:** Apache Kafka
    - Топик: `OrderCreated`

3. **Платформа:** Kubernetes
    - Приложение работает на порту `8005`.
    - Секреты для подключения к БД задаются через Kubernetes Secrets.

4. **Java:** OpenJDK 17 или выше.

5. **Инструменты сборки:** Gradle 7.5 или выше.

## Инструкция по сборке и установке

### Сборка приложения

1. Убедитесь, что установлен Gradle и JDK 17+.
2. Клонируйте репозиторий:
```bash
   git clone https://github.com/your-repository/beans-insight.git
   cd beans-insight
```
Соберите проект:

    ./gradlew build

Примените файл:

    kubectl apply -f clickhouse-secret.yaml

Создайте необходимые PersistentVolume и PersistentVolumeClaim для ClickHouse.

Разверните ClickHouse, используя манифесты:

    kubectl apply -f clickhouse-deployment.yaml

Примените манифест для развертывания микросервиса:

    kubectl apply -f app-deployment.yaml

### **Проверка работоспособности**

Убедитесь, что все поды запущены:

    kubectl get pods

Проверьте доступность API:

    curl http://<application-svc-ip>:8005/insight/{coffeeShopId}

Endpoints API

    GET /insight/{coffeeShopId} – Общая статистика кофейни.
    GET /insight/{coffeeShopId}/profit – Выручка за месяц.
    GET /insight/{coffeeShopId}/items – Топ товаров за месяц.

Используемые технологии

    Язык программирования: Kotlin
    Фреймворк: Spring Boot
    База данных: ClickHouse
    Очередь сообщений: Apache Kafka
    Контейнеризация: Docker, Kubernetes
    Паттерны: CQRS, Event Sourcing