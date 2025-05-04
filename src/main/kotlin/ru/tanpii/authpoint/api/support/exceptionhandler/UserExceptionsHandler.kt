package ru.tanpii.authpoint.api.support.exceptionhandler

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.tanpii.authpoint.domain.model.exception.UserAlreadyExistsException
import ru.tanpii.authpoint.domain.model.exception.UserNotFoundException
import ru.tanpii.authpoint.domain.model.exception.UserUnauthorizedException
import ru.tanpii.authpoint.domain.model.exception.WrongPasswordException
import ru.tanpii.authpoint.api.model.response.ExceptionResponse

@ControllerAdvice
class UserExceptionsHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler
    fun handleWrongPasswordException(ex: WrongPasswordException) = makeResponse(HttpStatus.FORBIDDEN, ex)

    @ExceptionHandler
    fun handleUserAlreadyExistsException(ex: UserAlreadyExistsException) = makeResponse(HttpStatus.CONFLICT, ex)

    @ExceptionHandler
    fun handleUserUnauthorizedException(ex: UserUnauthorizedException) = makeResponse(HttpStatus.UNAUTHORIZED, ex)

    @ExceptionHandler
    fun handleUserNotFoundException(ex: UserNotFoundException) = makeResponse(HttpStatus.NOT_FOUND, ex)

    private fun makeResponse(status: HttpStatus, ex: Exception) = ResponseEntity(
        ExceptionResponse(
            status.toString(),
            ex.message ?: "An unexpected error occurred"
        ),
        status
    )
}