package ru.cib.terraformbot.domain

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.Access
import javax.persistence.AccessType
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "terraform_sessions")
@Access(AccessType.FIELD)
data class Session(
    var chatId: Long? = null,
    var status: Boolean? = false,
    var path: String? = null,
    var privateKey: String? = null,
    var ip: String? = null,
    @field:CreationTimestamp
    var crDt: Date? = null
) : Domain()