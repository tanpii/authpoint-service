package ru.tanpii.authpoint.support.mapper

import ru.tanpii.authpoint.domain.model.dto.UserPrivateData
import ru.tanpii.authpoint.domain.model.dto.UserData
import ru.tanpii.authpoint.domain.model.entity.UserDataEntity

fun UserDataEntity.toUserData() = UserData(
    uuid = uuid,
    email = email,
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate,
    photoUrl = photoUrl
)

fun UserDataEntity.toUserPrivateData() = UserPrivateData(
    uuid = uuid,
    email = email,
    password = password,
    salt = salt,
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate,
    photoUrl = photoUrl
)