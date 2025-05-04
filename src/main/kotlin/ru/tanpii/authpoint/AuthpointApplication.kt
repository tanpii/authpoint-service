package ru.tanpii.authpoint

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.tanpii.authpoint.support.properties.AuthpointKafkaProperties
import ru.tanpii.authpoint.support.properties.ManagementApiProperties
import ru.tanpii.authpoint.support.properties.WebClientProperties

@SpringBootApplication
@EnableConfigurationProperties(
    WebClientProperties::class,
    AuthpointKafkaProperties::class,
    ManagementApiProperties::class
)
class AuthpointApplication

fun main(args: Array<String>) {
    runApplication<AuthpointApplication>(*args)
}
