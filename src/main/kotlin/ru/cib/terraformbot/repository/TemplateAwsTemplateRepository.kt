package ru.cib.terraformbot.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.cib.terraformbot.domain.TerraformAwsTemplate

interface TemplateAwsTemplateRepository : JpaRepository<TerraformAwsTemplate, Long> {
}