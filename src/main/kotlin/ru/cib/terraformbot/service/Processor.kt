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
    @Value("\${path-terraform}")
    private val terraformPath: String? = null,
    private val terraformConfig: TerraformConfig,
    private val sessionRepository: SessionRepository,
    private val rsa: RsaGenerator,
    private val terraform: TerraformFileGenerator
) {

    fun create(chatId: Long): Pair<String, File> {
        val session = sessionRepository.save(Session().apply {
            this.chatId = chatId
            this.status = false
            this.path = terraformPath + chatId
        })
        terraform.createDir(session.path!!)
        val keyPair = rsa.generate(session.path!!)
        terraform.create(keyPair.second.readText(), session.path!!, "$chatId")
        val ip = execCreate(session.path!!)
        sessionRepository.save(session.apply {
            this.status = true
            this.ip = ip
            this.privateKey = keyPair.first.readText()
            this.publicKey = keyPair.second.readText()
        })
        return Pair("$ip - started", keyPair.first)
    }

    fun destroy(chatId: Long): String {
        val session = sessionRepository.findByChatIdAndStatus(chatId, true)
        execDestroy(session.path!!)
        val result = sessionRepository.save(session.apply {
            this.status = false
        })
        return "${result.ip!!} - destroyed"
    }

    private fun execCreate(path: String): String {
        val ip = mutableListOf<String>()
        TerraformClient(terraformConfig.configuration()).use { client ->
            clientConfiguration(client, ip)
            client.workingDirectory = File(path)
            client.plan().get()
            client.apply().get()
        }
        return ip.last()
    }

    private fun execDestroy(path: String): String {
        val ip = mutableListOf<String>()
        TerraformClient(terraformConfig.configuration()).use { client ->
            clientConfiguration(client, ip)
            client.workingDirectory = File(path)
            client.destroy().get()
        }
        return ip.last()
    }

    private fun clientConfiguration(client: TerraformClient, ip: MutableList<String>) = client.apply {
        outputListener = Consumer {
            logger.debug(it)
            if (it.contains("public_id")) {
                ip.add(it.replace(Regex("[^0-9.]"), ""))
            }
        }
        errorListener = Consumer {
            logger.debug(it)
            if (it.contains("public_id")) {
                ip.add(it.replace(Regex("[^0-9.]"), ""))
            }
        }
    }
}