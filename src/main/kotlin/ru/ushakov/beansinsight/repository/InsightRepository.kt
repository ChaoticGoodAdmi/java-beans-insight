package ru.ushakov.beansinsight.repository

import org.springframework.stereotype.Repository
import ru.ushakov.beansinsight.domain.TopItem
import java.math.BigDecimal
import java.math.RoundingMode
import javax.sql.DataSource

@Repository
class AnalyticsRepository(private val dataSource: DataSource){

    fun getTotalOrders(coffeeShopId: String): Long {
        val sql = "SELECT COUNT(*) FROM orders_raw WHERE coffee_shop_id = ?"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, coffeeShopId)
                statement.executeQuery().use { resultSet ->
                    if (resultSet.next()) resultSet.getLong(1) else 0L
                }
            }
        }
    }

    fun getAverageCheck(coffeeShopId: String): BigDecimal {
        val sql = "SELECT CAST(AVG(total_cost) AS Decimal(18, 2)) AS average_check FROM orders_raw WHERE coffee_shop_id = ?"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, coffeeShopId)
                statement.executeQuery().use { resultSet ->
                    if (resultSet.next()) {
                        resultSet.getBigDecimal(1)?.setScale(2, RoundingMode.HALF_UP) ?: BigDecimal.ZERO
                    } else {
                        BigDecimal.ZERO
                    }
                }
            }
        }
    }

    fun getProfitForLastMonth(coffeeShopId: String): BigDecimal {
        val sql = """
            SELECT SUM(total_cost)
            FROM orders_raw
            WHERE coffee_shop_id = ? AND created_at >= now() - INTERVAL 1 MONTH
        """
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, coffeeShopId)
                statement.executeQuery().use { resultSet ->
                    if (resultSet.next()) resultSet.getBigDecimal(1) ?: BigDecimal.ZERO else BigDecimal.ZERO
                }
            }
        }
    }

    fun getTopItems(coffeeShopId: String): List<TopItem> {
        val sql = """
            SELECT product_id, SUM(quantity) as total_quantity
            FROM orders_raw
            WHERE coffee_shop_id = ? AND created_at >= now() - INTERVAL 1 MONTH
            GROUP BY product_id
            ORDER BY total_quantity DESC
            LIMIT 10
        """
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, coffeeShopId)
                statement.executeQuery().use { resultSet ->
                    val topItems = mutableListOf<TopItem>()
                    while (resultSet.next()) {
                        topItems.add(
                            TopItem(
                                productId = resultSet.getString("product_id"),
                                totalQuantity = resultSet.getInt("total_quantity")
                            )
                        )
                    }
                    topItems
                }
            }
        }
    }
}