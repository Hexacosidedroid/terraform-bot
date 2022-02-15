package ru.cib.terraformbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TerraformBotApplication

fun main(args: Array<String>) {
    runApplication<TerraformBotApplication>(*args)
}
