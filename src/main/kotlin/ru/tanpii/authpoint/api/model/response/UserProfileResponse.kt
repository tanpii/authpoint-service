package ru.tanpii.authpoint.api.model.response

import ru.tanpii.authpoint.domain.model.dto.BookingInfo
import ru.tanpii.authpoint.domain.model.dto.UserData

data class UserProfileResponse(
    val userData: UserData,
    val bookInfo: BookingInfo?
)
