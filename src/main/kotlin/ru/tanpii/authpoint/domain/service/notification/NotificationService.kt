package ru.tanpii.authpoint.domain.service.notification

import ru.tanpii.authpoint.domain.model.dto.notification.Event

interface NotificationService {
    fun sendNotification(notification: Event)
}
