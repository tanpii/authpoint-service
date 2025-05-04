package ru.tanpii.authpoint.domain.model.dto.notification

import ru.tanpii.authpoint.domain.model.type.NotificationType

interface Event {
    val email: String
    val eventType: NotificationType
}
