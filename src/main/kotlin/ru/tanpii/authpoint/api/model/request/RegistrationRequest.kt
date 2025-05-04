package ru.tanpii.authpoint.api.model.request

import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

data class RegistrationRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val profileImage: MultipartFile?
)
