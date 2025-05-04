package ru.tanpii.authpoint.api.rest

import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.tanpii.authpoint.api.model.request.AuthenticationRequest
import ru.tanpii.authpoint.api.model.request.RegistrationRequest
import ru.tanpii.authpoint.api.model.response.TokenResposne
import ru.tanpii.authpoint.domain.service.JwtService
import ru.tanpii.authpoint.domain.service.user.UserService

private val logger = KotlinLogging.logger { }

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val jwtService: JwtService,
    private val userService: UserService
) {

    @Operation(
        summary = "Аутентификация пользователя",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Успешный вход",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = TokenResposne::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверные данные",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class)
                    ),
                    Content(
                        mediaType = "application/problem+json",
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Неверный пароль",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Пользователь не найден",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @PostMapping("/signin")
    fun signin(@RequestBody authenticationRequest: AuthenticationRequest): TokenResposne {
        val userUuid = userService.verifyUser(authenticationRequest)
        val token = jwtService.generate(userUuid)
        return TokenResposne(token)
    }

    @PostMapping(
        "/signup",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Регистрация пользователя",
        responses = [
            ApiResponse(responseCode = "200", description = "Успешная регистрация"),
            ApiResponse(
                responseCode = "400",
                description = "Неверные данные",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Пользователь уже существует",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "415",
                description = "Неверный Content-Type",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class)
                    ),
                    Content(
                        mediaType = "application/problem+json",
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            )
        ]
    )
    fun signup(
        @Parameter(
            description = "Данные регистрации",
            required = true,
            content = [Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)]
        )
        @ModelAttribute registrationRequest: RegistrationRequest
    ): TokenResposne {
        logger.info { registrationRequest }
        val userUuid = userService.createUser(registrationRequest)
        val token = jwtService.generate(userUuid)
        return TokenResposne(token)
    }
}