package ru.tanpii.authpoint.domain.model.dto.notification

import ru.tanpii.authpoint.domain.model.type.NotificationType

data class RegistrationNotification(
    override val email: String,
    override val eventType: NotificationType,
    val firstName: String
) : Event
