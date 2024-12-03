package ru.ushakov.beansinsight.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    @Value("\${spring.datasource.url}")
    lateinit var url: String

    @Value("\${CLICKHOUSE_USERNAME:default}")
    lateinit var username: String

    @Value("\${CLICKHOUSE_PASSWORD:}")
    lateinit var password: String

    @Bean
    fun clickHouseDataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("com.clickhouse.jdbc.ClickHouseDriver")
        dataSource.url = url
        dataSource.username = username
        dataSource.password = password
        return dataSource
    }
}