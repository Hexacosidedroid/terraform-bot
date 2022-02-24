package ru.cib.terraformbot.domain

import javax.persistence.*

@MappedSuperclass
@Access(AccessType.FIELD)
abstract class Domain {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Basic(optional = false)
    @field:Column(name = "id", nullable = false, updatable = false, insertable = false)
    open var id: Long? = null
}