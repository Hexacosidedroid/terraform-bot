package ru.cib.terraformbot.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.cib.terraformbot.domain.TerraformAwsTemplate
import ru.cib.terraformbot.repository.TemplateAwsTemplateRepository
import java.io.File
import javax.annotation.PostConstruct

@Service
class TerraformFileGenerator(
    @Value("\${path-terraform}")
    private val terraformPath: String? = null,
    @Value("\${file.separator}")
    private val fileSeparator: String? = null,
    private val terraformTemplate: TemplateAwsTemplateRepository
) {

    @PostConstruct
    fun init() {
        if (terraformTemplate.findAll().isEmpty()) {
            val path = "$terraformPath${fileSeparator}terraform"
            val templates = TerraformAwsTemplate().apply {
                main = File("$path${fileSeparator}main.tf").readText(Charsets.UTF_8)
                output = File("$path${fileSeparator}output.tf").readText(Charsets.UTF_8)
                provider = File("$path${fileSeparator}provider.tf").readText(Charsets.UTF_8)
                security = File("$path${fileSeparator}security.tf").readText(Charsets.UTF_8)
                variable = File("$path${fileSeparator}variable.tf").readText(Charsets.UTF_8)
                keyPair = File("$path${fileSeparator}key_pair.tf").readText(Charsets.UTF_8)
            }
            terraformTemplate.save(templates)
        }
    }

    fun createDir(path: String) {
        val file = File(path)
        if (!file.exists())
            file.mkdir()
    }

    fun create(privateKey: String, path: String) {
        val templates = terraformTemplate.findById(1).get()
        createKeyPair(path, privateKey, templates.keyPair)
        createMain(path, templates.main)
        createSecurity(path, templates.security)
        createVariable(path, templates.variable)
        createProvider(path, templates.provider)
        createOutput(path, templates.output)
    }

    private fun createMain(path: String, main: String?) {
        val file = File("$path${fileSeparator}main.tf")
        file.writeText(main!!, Charsets.UTF_8)
    }

    private fun createVariable(path: String, variable: String?) {
        val file = File("$path${fileSeparator}variable.tf")
        file.writeText(variable!!, Charsets.UTF_8)
    }

    private fun createProvider(path: String, provider: String?) {
        val file = File("$path${fileSeparator}provider.tf")
        file.writeText(provider!!, Charsets.UTF_8)
    }

    private fun createOutput(path: String, output: String?) {
        val file = File("$path${fileSeparator}output.tf")
        file.writeText(output!!, Charsets.UTF_8)
    }

    private fun createKeyPair(path: String, privateKey: String, keyPair: String?) {
        val file = File("$path${fileSeparator}key_pair.tf")
        val keyPairReplaced = keyPair!!.replace("placeholder", privateKey, true)
        file.writeText(keyPairReplaced, Charsets.UTF_8)
    }

    private fun createSecurity(path: String, security: String?) {
        val file = File("$path${fileSeparator}security.tf")
        file.writeText(security!!, Charsets.UTF_8)
    }
}