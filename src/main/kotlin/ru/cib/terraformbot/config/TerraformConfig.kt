package ru.cib.terraformbot.config

import com.microsoft.terraform.TerraformAwsOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TerraformConfig {
    @Value("\${terraform.awsAccessKeyId}")
    val awsAccessKeyId: String? = null
    @Value("\${terraform.awsSecretAccessKey}")
    val awsSecretAccessKey: String? = null

    @Bean
    fun configuration() = TerraformAwsOptions().apply {
        accessKeyId = awsAccessKeyId
        secretAccessKey = awsSecretAccessKey
    }
}