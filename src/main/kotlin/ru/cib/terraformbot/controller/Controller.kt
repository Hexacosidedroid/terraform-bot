package ru.cib.terraformbot.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.cib.terraformbot.service.Processor

@RestController
class Controller(
    private val processor: Processor
) {

//    @GetMapping("/create")
//    fun create(): String = processor.create()
//
//    @GetMapping("/destroy")
//    fun destroy(): String = processor.destroy()
}