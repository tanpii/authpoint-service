package ru.tanpii.authpoint.api.grpc

import net.devh.boot.grpc.server.service.GrpcService
import ru.tanpii.authpoint.domain.service.JwtService
import ru.tanpii.authpoint.domain.service.user.UserService
import ru.tanpii.authpoint.support.mapper.toTimestamp
import ru.tanpii.authpoint.support.mapper.toUuid
import ru.tanpii.authpoint.user.GetUserDataIdRequest
import ru.tanpii.authpoint.user.GetUserDataJwtRequest
import ru.tanpii.authpoint.user.UserDataServiceGrpcKt.UserDataServiceCoroutineImplBase
import ru.tanpii.authpoint.user.userDataResponse
import java.util.*

@GrpcService
class UserDataGrpcService(
    private val userService: UserService,
    private val jwtService: JwtService
) : UserDataServiceCoroutineImplBase() {

    override suspend fun getUserDataByJwt(request: GetUserDataJwtRequest) =
        getUserInfo(jwtService.getUserId(request.jwt))

    override suspend fun getUserDataById(request: GetUserDataIdRequest) =
        getUserInfo(request.uuid.toUuid())

    private fun getUserInfo(userId: UUID) =
        userService.getUserData(userId).let {
            userDataResponse {
                uuid = it.uuid.toString()
                email = it.email
                firstName = it.firstName
                lastName = it.lastName
                birthdate = it.birthDate.toTimestamp()
                photoUrl = it.photoUrl ?: ""
            }
        }
}
