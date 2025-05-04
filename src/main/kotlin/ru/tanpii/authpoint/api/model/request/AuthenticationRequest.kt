package ru.tanpii.authpoint.api.model.request

data class AuthenticationRequest(
    val email: String,
    val password: String
)
