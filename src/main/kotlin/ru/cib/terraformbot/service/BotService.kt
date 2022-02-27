package ru.cib.terraformbot.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class BotService(
    private val processor: Processor
) : TelegramLongPollingBot() {
    @Value("\${bot.name}")
    val name: String? = null

    @Value("\${bot.token}")
    val token: String? = null

    override fun getBotToken() = token

    override fun getBotUsername() = name

    override fun onUpdateReceived(update: Update) {
        val sessionGuid = UUID.randomUUID().toString()
        logger.debug("[$sessionGuid] Initialize session with bot")
        try {
            if (update.hasMessage()) {
                val message = update.message
                val chatId = message.chatId
                logger.debug("[$sessionGuid] Chat started for id: $chatId")
                when (message.text) {
                    "/start" -> {
                        execute(SendMessage("$chatId", "Welcome! This bot designed for VM's creation"))
                    }
                    "/help" -> {
                        execute(SendMessage("$chatId", "/create - create new instance, /destroy - destroy current instance"))
                    }
                    "/create" -> {
                        execute(SendMessage("$chatId", "Generating started"))
                        val response = processor.create(chatId)
                        execute(SendMessage("$chatId", response.first))
                        execute(SendDocument("$chatId", InputFile(response.second)))
                    }
                    "/destroy" -> {
                        execute(SendMessage("$chatId", "Destroying started"))
                        val response = processor.destroy(chatId)
                        execute(SendMessage("$chatId", response))
                    }
                    else -> {
                        execute(SendMessage("$chatId", "Enter /help for commands"))
                    }
                }
                logger.debug("[$sessionGuid] Forming response message and sending back")
            } else
                throw Exception("Message is empty!!!")
        } catch (e: Exception) {
            logger.error("[$sessionGuid]: Error during receiving message: ${e.message}")
        }
    }
}