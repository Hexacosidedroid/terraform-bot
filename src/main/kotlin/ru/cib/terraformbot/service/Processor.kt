package ru.cib.terraformbot.service

import com.microsoft.terraform.TerraformClient
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.cib.terraformbot.config.TerraformConfig
import java.io.File
import java.util.function.Consumer

private val logger = KotlinLogging.logger {}

@Service
class Processor(
    private val terraformConfig: TerraformConfig
) {
    @Value("\${path}")
    private val path: String? = null

    fun create(): Boolean = TerraformClient(terraformConfig.configuration()).use { client ->
        clientConfiguration(client)
        client.workingDirectory = File(path)
        client.plan().get()
        client.apply().get()
    }

    fun destroy(): Boolean = TerraformClient(terraformConfig.configuration()).use { client ->
        clientConfiguration(client)
        client.workingDirectory = File(path)
        client.destroy().get()
    }

    private fun clientConfiguration(client: TerraformClient) = client.apply {
        outputListener = Consumer {
            logger.debug(it)
        }
        errorListener = Consumer {
            logger.debug(it)
        }
    }
}