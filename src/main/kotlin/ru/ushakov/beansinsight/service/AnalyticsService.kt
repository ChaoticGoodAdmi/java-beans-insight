package ru.ushakov.beansinsight.service

import org.springframework.stereotype.Service
import ru.ushakov.beansinsight.OrderItem
import ru.ushakov.beansinsight.Order
import java.sql.Connection
import javax.sql.DataSource

@Service
class AnalyticsService(private val dataSource: DataSource) {

    fun processOrder(order: Order) {
        dataSource.connection.use { connection ->
            order.items.forEach { item ->
                saveOrder(connection, order, item)
            }
        }
    }

    private fun saveOrder(connection: Connection, order: Order, item: OrderItem) {
        val sql = """
            INSERT INTO orders_raw (order_id, user_id, coffee_shop_id, product_id, quantity, price, total_cost, created_at, status) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """
        connection.prepareStatement(sql).use { statement ->
            statement.setLong(1, order.orderId)
            statement.setString(2, order.userId)
            statement.setString(3, order.coffeeShopId)
            statement.setString(4, item.productId)
            statement.setInt(5, item.quantity)
            statement.setBigDecimal(6, item.price)
            statement.setBigDecimal(7, order.totalCost)
            statement.setTimestamp(8, java.sql.Timestamp.valueOf(order.createdAt))
            statement.setString(9, order.status.name)
            statement.executeUpdate()
        }
    }
}