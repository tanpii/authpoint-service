package ru.tanpii.authpoint.support.properties

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka")
class AuthpointKafkaProperties {
    var clusters: Map<String, KafkaProperties> = mutableMapOf()
}
