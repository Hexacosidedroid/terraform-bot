package ru.cib.terraformbot.service

import com.jcraft.jsch.JSch
import com.jcraft.jsch.KeyPair
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

private val logger = KotlinLogging.logger {}

@Service
class RsaGenerator(
    @Value("\${file.separator}")
    private val fileSeparator: String? = null,
) {

    fun generate(path: String): Pair<String, File> {
        logger.info("Starting to create Key Pair")
        val privateKey = "$path${fileSeparator}id_rsa"
        val publicKey = "$path${fileSeparator}id_rsa.pub"
        if (File(publicKey).exists() || File(privateKey).exists()) {
            File(publicKey).delete()
            File(privateKey).delete()
        }
        val keyPair = KeyPair.genKeyPair(JSch(), KeyPair.RSA)
        keyPair.writePrivateKey(privateKey)
        keyPair.writePublicKey(publicKey, "public_key")
        keyPair.dispose()
        logger.info("Creation complete! Finger print ${keyPair.fingerPrint}")
        return getKeys(path)
    }

    private fun getKeys(path: String): Pair<String, File> {
        val privateKey = File("$path${fileSeparator}id_rsa").readText()
        val publicKey = File("$path${fileSeparator}id_rsa.pub")
        return Pair(privateKey, publicKey)
    }
}