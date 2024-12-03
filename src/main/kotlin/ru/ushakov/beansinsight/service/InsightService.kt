package ru.ushakov.beansinsight.service

import org.springframework.stereotype.Service
import ru.ushakov.beansinsight.domain.CoffeeShopStats
import ru.ushakov.beansinsight.domain.TopItem
import ru.ushakov.beansinsight.repository.AnalyticsRepository
import java.math.BigDecimal

@Service
class InsightService(private val analyticsRepository: AnalyticsRepository) {

    fun getCoffeeShopStats(coffeeShopId: String): CoffeeShopStats {
        val totalOrders = analyticsRepository.getTotalOrders(coffeeShopId)
        val avgCheck = analyticsRepository.getAverageCheck(coffeeShopId)
        return CoffeeShopStats(
            coffeeShopId = coffeeShopId,
            totalOrders = totalOrders,
            averageCheck = avgCheck
        )
    }

    fun getCoffeeShopProfit(coffeeShopId: String): BigDecimal {
        return analyticsRepository.getProfitForLastMonth(coffeeShopId)
    }

    fun getTopItems(coffeeShopId: String): List<TopItem> {
        return analyticsRepository.getTopItems(coffeeShopId)
    }
}