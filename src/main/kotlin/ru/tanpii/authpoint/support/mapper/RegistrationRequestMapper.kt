package ru.tanpii.authpoint.support.mapper

import ru.tanpii.authpoint.domain.model.dto.UserPrivateData
import ru.tanpii.authpoint.api.model.request.RegistrationRequest
import java.util.*

fun RegistrationRequest.toUserPrivateData(uuid: UUID, password: String, salt: String, photoUrl: String?) =
    UserPrivateData(
        uuid = uuid,
        email = email,
        password = password,
        salt = salt,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
        photoUrl = photoUrl
    )
