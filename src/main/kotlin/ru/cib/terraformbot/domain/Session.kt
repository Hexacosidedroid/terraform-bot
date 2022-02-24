package ru.cib.terraformbot.domain

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.Access
import javax.persistence.AccessType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "terraform_sessions")
@Access(AccessType.FIELD)
data class Session(
    var chatId: Long? = null,
    var status: Boolean? = false,
    var path: String? = null,
    @Column(length = 1000)
    var privateKey: String? = null,
    @Column(length = 3000)
    var publicKey: String? = null,
    var ip: String? = null,
    var qr: ByteArray? = null,
    @field:CreationTimestamp
    var crDt: Date? = null
) : Domain()