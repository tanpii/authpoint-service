package ru.tanpii.authpoint.domain.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

private const val CLIENT_ID_FIELD = "client"

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val key: String
) {
    fun generate(userId: UUID): String =
        Jwts
            .builder()
            .signWith(getSigningKey())
            .claims()
            .add(CLIENT_ID_FIELD, userId.toString())
            .and()
            .compact()

    fun getUserId(token: String): UUID = try {
        Jwts
            .parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
            .get(CLIENT_ID_FIELD, String::class.java)
            .let { UUID.fromString(it) }
    } catch (ex: Exception) {
        throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid token")
    }


    private fun getSigningKey(): SecretKey {
        val keyBytes: ByteArray = key.toByteArray(StandardCharsets.UTF_8)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}