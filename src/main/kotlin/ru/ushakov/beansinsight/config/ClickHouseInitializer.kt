package ru.ushakov.beansinsight.config

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.sql.Connection
import javax.sql.DataSource

@Service
class ClickHouseInitializer(private val dataSource: DataSource) {

    @PostConstruct
    fun initializeTables() {
        dataSource.connection.use { connection ->
            createRawOrdersTable(connection)
            createAnalyticsTable(connection)
        }
    }

    private fun createRawOrdersTable(connection: Connection) {
        val sql = """
            CREATE TABLE IF NOT EXISTS orders_raw (
                order_id UInt64,
                user_id String,
                coffee_shop_id String,
                product_id String,
                quantity UInt32,
                price Decimal(10, 2),
                total_cost Decimal(10, 2),
                created_at DateTime64(3),
                status String
            ) ENGINE = MergeTree()
            PARTITION BY toYYYYMM(created_at)
            ORDER BY (coffee_shop_id, created_at)
        """
        connection.createStatement().execute(sql)
    }

    private fun createAnalyticsTable(connection: Connection) {
        val sql = """
            CREATE TABLE IF NOT EXISTS coffee_shop_analytics (
                coffee_shop_id String,
                total_orders UInt64,
                revenue Decimal(18, 2),
                avg_order_value Decimal(18, 2),
                calculated_at DateTime64(3)
            ) ENGINE = SummingMergeTree()
            PARTITION BY toYYYYMM(calculated_at)
            ORDER BY (coffee_shop_id, calculated_at)
        """
        connection.createStatement().execute(sql)
    }
}