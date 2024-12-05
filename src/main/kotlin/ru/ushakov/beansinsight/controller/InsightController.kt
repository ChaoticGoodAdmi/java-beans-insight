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
    fun getCoffeeShopStats(@RequestHeader(name = "X-CoffeeShopId", required = true) @NotBlank coffeeShopId: String): CoffeeShopStats {
        return insightService.getCoffeeShopStats(coffeeShopId)
    }

    @GetMapping("/profit")
    fun getCoffeeShopProfit(@RequestHeader(name = "X-CoffeeShopId", required = true) @NotBlank coffeeShopId: String): Map<String, BigDecimal> {
        return mapOf("profit" to insightService.getCoffeeShopProfit(coffeeShopId))
    }

    @GetMapping("/items")
    fun getTopItems(@RequestHeader(name = "X-CoffeeShopId", required = true) @NotBlank coffeeShopId: String): Map<String, List<TopItem>> {
        return mapOf("topItems" to insightService.getTopItems(coffeeShopId))
    }
}