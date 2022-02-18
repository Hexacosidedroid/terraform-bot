package ru.cib.terraformbot.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
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
                val response = when (message.text) {
                    "/start" -> "Welcome! This bot designed for create VM's"
                    "/help" -> "create, destroy"
                    "/create" -> processor.create(chatId)
                    "/destroy" -> processor.destroy(chatId)
                    else -> "Enter /help for commands"
                }
                logger.debug("[$sessionGuid] Forming response message and sending back")
                execute(SendMessage("$chatId", response))
            } else
                throw Exception("Message is empty!!!")
        } catch (e: Exception) {
            logger.error("[$sessionGuid]: Error during receiving message: ${e.message}")
        }
    }
}