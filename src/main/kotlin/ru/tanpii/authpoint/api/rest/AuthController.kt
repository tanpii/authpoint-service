package ru.tanpii.authpoint.api.rest

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

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val jwtService: JwtService,
    private val userService: UserService
) {

    @PostMapping("/signin")
    fun signin(@RequestBody authenticationRequest: AuthenticationRequest): TokenResposne {
        val userUuid = userService.verifyUser(authenticationRequest)
        val token = jwtService.generate(userUuid)
        return TokenResposne(token)
    }

    @PostMapping("/signup")
    fun signup(@ModelAttribute registrationRequest: RegistrationRequest): TokenResposne {
        val userUuid = userService.createUser(registrationRequest)
        val token = jwtService.generate(userUuid)
        return TokenResposne(token)
    }
}
