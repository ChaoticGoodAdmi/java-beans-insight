package ru.ushakov.beansinsight.controller

import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.*
import ru.ushakov.beansinsight.domain.CoffeeShopStats
import ru.ushakov.beansinsight.domain.TopItem
import ru.ushakov.beansinsight.service.InsightService
import java.math.BigDecimal

@RestController
@RequestMapping("/insight")
class InsightController(private val insightService: InsightService) {

    @GetMapping("/stats")
    fun getCoffeeShopStats(@RequestHeader(name = "X-CoffeeShopId", required = true) coffeeShopId: String): CoffeeShopStats {
        check(coffeeShopId.isNotBlank()) { "User is not attached to any coffee-shops" }
        return insightService.getCoffeeShopStats(coffeeShopId)
    }

    @GetMapping("/profit")
    fun getCoffeeShopProfit(@RequestHeader(name = "X-CoffeeShopId", required = true) coffeeShopId: String): Map<String, BigDecimal> {
        check(coffeeShopId.isNotBlank()) { "User is not attached to any coffee-shops" }
        return mapOf("profit" to insightService.getCoffeeShopProfit(coffeeShopId))
    }

    @GetMapping("/items")
    fun getTopItems(@RequestHeader(name = "X-CoffeeShopId", required = true) coffeeShopId: String): Map<String, List<TopItem>> {
        check(coffeeShopId.isNotBlank()) { "User is not attached to any coffee-shops" }
        return mapOf("topItems" to insightService.getTopItems(coffeeShopId))
    }
}