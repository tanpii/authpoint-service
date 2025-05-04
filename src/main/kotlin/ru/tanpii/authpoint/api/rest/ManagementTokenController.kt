package ru.tanpii.authpoint.api.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/management/token")
class ManagementTokenController {

    @PostMapping("/verify")
    @Operation(
        responses = [
            ApiResponse(responseCode = "200", description = "Успешный запрос"),
            ApiResponse(responseCode = "400", description = "Некорреткный запрос"),
            ApiResponse(responseCode = "403", description = "Невалидный токен")
        ]
    )
    fun verifyManagementToken(@RequestBody tokenRequest: ManagementTokenRequest) {
        if (tokenRequest.token != "bookpoint") {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid token")
        }
    }
}

data class ManagementTokenRequest(
    val token: String
)
