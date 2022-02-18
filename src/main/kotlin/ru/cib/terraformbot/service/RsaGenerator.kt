package ru.cib.terraformbot.service

import org.springframework.stereotype.Service
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey

@Service
class RsaGenerator {

    fun generate(): Pair<PrivateKey, PublicKey> {
        var pair: KeyPair
        KeyPairGenerator.getInstance("RSA").apply {
            initialize(2048)
            pair = generateKeyPair()
            return Pair(pair.private, pair.public)
        }
    }
}