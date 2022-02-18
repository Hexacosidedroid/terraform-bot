package ru.cib.terraformbot.service

import com.microsoft.terraform.TerraformClient
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.cib.terraformbot.config.TerraformConfig
import ru.cib.terraformbot.domain.Session
import ru.cib.terraformbot.repository.SessionRepository
import java.io.File
import java.util.function.Consumer

private val logger = KotlinLogging.logger {}

@Service
class Processor(
    private val terraformConfig: TerraformConfig,
    private val sessionRepository: SessionRepository
) {
    @Value("\${path}")
    private val path: String? = null

    fun create(chatId: Long): String {
        //TODO generate new directory with terraform files
        val ip = execCreate(path!!)
        val result = sessionRepository.save(Session().apply {
            this.chatId = chatId
            this.status = true
            this.path = path
            this.privateKey = ""
            this.ip = ip
        })
        return result.ip!!
    }

    fun destroy(chatId: Long): String {
        val session = sessionRepository.findByChatId(chatId)
        execDestroy(path!!)
        val result = sessionRepository.save(session.apply {
            this.status = false
        })
        return result.ip!!
    }

//    private fun formFiles() {}

    private fun execCreate(path: String): String {
        val ip = mutableListOf<String>()
        TerraformClient(terraformConfig.configuration()).use { client ->
            clientConfiguration(client, ip)
            client.workingDirectory = File(path)
            client.plan().get()
            client.apply().get()
        }
        return ip[0]
    }

    private fun execDestroy(path: String): String {
        val ip = mutableListOf<String>()
        TerraformClient(terraformConfig.configuration()).use { client ->
            clientConfiguration(client, ip)
            client.workingDirectory = File(path)
            client.destroy().get()
        }
        return ip[0]
    }

    private fun clientConfiguration(client: TerraformClient, ip: MutableList<String>) = client.apply {
        outputListener = Consumer {
            logger.debug(it)
            if (it.contains("Public IP")) {
                ip.add(it)
            }
        }
        errorListener = Consumer {
            logger.debug(it)
            if (it.contains("Public IP")) {
                ip.add(it)
            }
        }
    }
}