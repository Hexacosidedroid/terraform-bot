package ru.cib.terraformbot.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.cib.terraformbot.domain.Session

interface SessionRepository : JpaRepository<Session, Long> {
    fun findByChatId(chatId: Long): Session
}