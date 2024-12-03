package ru.ushakov.beansinsight

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import ru.ushakov.beansinsight.service.AnalyticsService
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class OrderCreatedEventListener(
    private val analyticsService: AnalyticsService,
    private val objectMapper: ObjectMapper
) {

    @KafkaListener(topics = ["OrderCreated"], groupId = "beans-insight-group")
    fun consumeOrder(message: String) {
        val order = objectMapper.readValue(message, Order::class.java)
        println("OrderCreatedEvent received from Kafka: $order")
        analyticsService.processOrder(order)
    }
}

data class Order(
    val orderId: Long,
    val userId: String,
    val coffeeShopId: String,
    val items: List<OrderItem>,
    val totalCost: BigDecimal,
    val createdAt: LocalDateTime,
    val status: OrderStatus,
    val bonusPointsUsed: Int
)

enum class OrderStatus {
    CREATED, IN_PROGRESS, READY, DELIVERED
}

data class OrderItem(
    val productId: String,
    val quantity: Int,
    val price: BigDecimal
)

@Configuration
class JacksonConfig {
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
}