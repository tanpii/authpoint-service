package ru.tanpii.authpoint.domain.client

import ru.tanpii.authpoint.domain.model.dto.BookingInfo
import java.util.*

interface BookpointClient {
    fun getBookingInfo(userId: UUID): BookingInfo?
}
