package ru.cib.terraformbot.service

import org.apache.commons.lang3.time.DateUtils
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.cib.terraformbot.repository.SessionRepository
import java.util.*

@Service
@EnableScheduling
class Task(
    private val sessionRepository: SessionRepository,
    private val processor: Processor
) {

    @Scheduled(cron = "0 0 1 * * ?")
    fun clean() {
        val active = sessionRepository.findAllByStatus(true)
            .filter { DateUtils.isSameDay(DateUtils.addDays(Date(), -3), it.crDt) }
        active.forEach {
            processor.destroy(it.chatId!!)
        }
    }
}