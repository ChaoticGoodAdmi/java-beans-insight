package ru.ushakov.beansinsight.domain

import java.math.BigDecimal

data class CoffeeShopStats(
    val coffeeShopId: String,
    val totalOrders: Long,
    val averageCheck: BigDecimal
)

data class TopItem(
    val productId: String,
    val totalQuantity: Int
)