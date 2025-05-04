package ru.tanpii.authpoint.api.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.*
import ru.tanpii.authpoint.api.model.request.ChangeProfileDataRequest
import ru.tanpii.authpoint.api.model.response.UserProfileResponse
import ru.tanpii.authpoint.domain.service.JwtService
import ru.tanpii.authpoint.domain.service.user.UserService
import java.util.*

@RestController
@RequestMapping("/api/v1/user")
class UserDataController(
    private val userService: UserService,
    private val jwtService: JwtService
) {

    @GetMapping("/profile/self")
    @Operation(
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Успешный запрос",
                content = [Content(schema = Schema(implementation = UserProfileResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Некорреткный запрос",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Невалидный токен",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Не найден пользователь",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @SecurityRequirement(name = "bearerAuth")
    fun getData(@RequestHeader("Authorization") token: String): UserProfileResponse =
        token.replace("Bearer ", "").let { jwtToken ->
            userService.getUserProfileInfo(jwtService.getUserId(jwtToken))
        }

    @Operation(
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Успешный запрос",
                content = [Content(schema = Schema(implementation = UserProfileResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Некорреткный запрос",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Не найден пользователь",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile/{uuid}")
    fun getUserProfileData(@PathVariable uuid: UUID): UserProfileResponse =
        userService.getUserProfileInfo(uuid)

    @Operation(
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Успешный запрос",
                content = [Content(schema = Schema(implementation = UserProfileResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Некорреткный запрос",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Невалидный токен",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Не найден пользователь",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/profile/self")
    fun editProfile(@RequestHeader("Authorization") token: String, @ModelAttribute req: ChangeProfileDataRequest) {
        token.replace("Bearer ", "").let { jwtToken ->
            userService.editProfile(jwtService.getUserId(jwtToken), req)
        }
    }
}
