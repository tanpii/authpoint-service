package ru.tanpii.authpoint.domain.service.encryption

interface EncryptionService {
    fun encrypt(payload: String, salt: String): String

    fun generateSalt(): String

    fun isCorrect(payload: String, salt: String): Boolean
}