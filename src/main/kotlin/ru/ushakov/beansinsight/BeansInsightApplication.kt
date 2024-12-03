package ru.ushakov.beansinsight

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BeansInsightApplication

fun main(args: Array<String>) {
    runApplication<BeansInsightApplication>(*args)
}
