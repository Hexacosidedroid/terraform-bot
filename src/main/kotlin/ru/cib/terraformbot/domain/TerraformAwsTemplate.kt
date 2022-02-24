package ru.cib.terraformbot.domain

import javax.persistence.*

@Entity
@Table(name = "terraform_aws_template")
@Access(AccessType.FIELD)
data class TerraformAwsTemplate(
    @Column(length = 1000)
    var main: String? = null,
    @Column(length = 1000)
    var output: String? = null,
    @Column(length = 1000)
    var provider: String? = null,
    @Column(length = 1000)
    var security: String? = null,
    @Column(length = 1000)
    var variable: String? = null,
    @Column(length = 1000)
    var keyPair: String? = null
) : Domain()