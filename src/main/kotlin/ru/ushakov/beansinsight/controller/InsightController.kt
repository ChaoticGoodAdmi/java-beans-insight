package ru.ushakov.beansinsight.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ushakov.beansinsight.domain.CoffeeShopStats
import ru.ushakov.beansinsight.domain.TopItem
import ru.ushakov.beansinsight.service.InsightService
import java.math.BigDecimal

@RestController
@RequestMapping("/insight")
class InsightController(private val insightService: InsightService) {

    @GetMapping("/{coffeeShopId}")
    fun getCoffeeShopStats(@PathVariable coffeeShopId: String): CoffeeShopStats {
        return insightService.getCoffeeShopStats(coffeeShopId)
    }

    @GetMapping("/{coffeeShopId}/profit")
    fun getCoffeeShopProfit(@PathVariable coffeeShopId: String): Map<String, BigDecimal> {
        return mapOf("profit" to insightService.getCoffeeShopProfit(coffeeShopId))
    }

    @GetMapping("/{coffeeShopId}/items")
    fun getTopItems(@PathVariable coffeeShopId: String): Map<String, List<TopItem>> {
        return mapOf("topItems" to insightService.getTopItems(coffeeShopId))
    }
}