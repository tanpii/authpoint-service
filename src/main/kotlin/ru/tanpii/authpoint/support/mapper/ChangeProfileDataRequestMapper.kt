package ru.tanpii.authpoint.support.mapper

import ru.tanpii.authpoint.api.model.request.ChangeProfileDataRequest
import ru.tanpii.authpoint.domain.model.dto.ChangedProfileData

fun ChangeProfileDataRequest.toChangedProfileData(password: String?, salt: String?, photoUrl: String?) =
    ChangedProfileData(
        email = email,
        password = password,
        salt = salt,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
        photoUrl = photoUrl
    )
