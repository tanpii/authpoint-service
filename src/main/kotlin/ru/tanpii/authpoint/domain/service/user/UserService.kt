package ru.tanpii.authpoint.domain.service.user

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import ru.tanpii.authpoint.domain.model.exception.UserAlreadyExistsException
import ru.tanpii.authpoint.domain.model.exception.WrongPasswordException
import ru.tanpii.authpoint.api.model.request.AuthenticationRequest
import ru.tanpii.authpoint.api.model.request.ChangeProfileDataRequest
import ru.tanpii.authpoint.api.model.request.RegistrationRequest
import ru.tanpii.authpoint.api.model.response.UserProfileResponse
import ru.tanpii.authpoint.domain.client.BookpointClient
import ru.tanpii.authpoint.domain.model.dto.notification.RegistrationNotification
import ru.tanpii.authpoint.domain.model.dto.UserData
import ru.tanpii.authpoint.domain.model.dto.UserPrivateData
import ru.tanpii.authpoint.domain.model.type.NotificationType
import ru.tanpii.authpoint.domain.service.encryption.EncryptionService
import ru.tanpii.authpoint.domain.service.image.ImageService
import ru.tanpii.authpoint.domain.service.notification.NotificationService
import ru.tanpii.authpoint.domain.service.user.component.UserDataComponent
import ru.tanpii.authpoint.support.mapper.toChangedProfileData
import ru.tanpii.authpoint.support.mapper.toUserPrivateData
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class UserService(
    private val userDataComponent: UserDataComponent,
    private val encryptionService: EncryptionService,
    private val imageService: ImageService,
    private val notificationService: NotificationService,
    private val bookpointClient: BookpointClient
) {

    fun createUser(request: RegistrationRequest): UUID {
        logger.info { "Start creating user, email=${request.email}" }

        if (userExistsByEmail(request.email)) {
            throw UserAlreadyExistsException("User with email ${request.email} already exists")
        }

        val uuid = UUID.randomUUID()
        val photoUrl = request.profileImage?.let {
            imageService.uploadImage(it.inputStream, uuid.toString())
        }
        val passwordSalt = encryptionService.generateSalt()
        val hashedPassword = encryptionService.encrypt(request.password, passwordSalt)

        request.toUserPrivateData(uuid, hashedPassword, passwordSalt, photoUrl).apply {
            userDataComponent.createUserData(this)
        }.also { notifyUser(it) }

        logger.info { "End of creating user, email=${request.email}" }
        return uuid
    }

    fun verifyUser(request: AuthenticationRequest): UUID {
        logger.info { "Start verifying user, email=${request.email}" }

        val userPrivateData = userDataComponent.getUserPrivateData(request.email)
        val hashedPassword = encryptionService.encrypt(request.password, userPrivateData.salt)
        if (hashedPassword != userPrivateData.password) {
            throw WrongPasswordException("Password is incorrect")
        }

        logger.info { "End of verifying user, email=${request.email}" }
        return userPrivateData.uuid
    }

    fun userExistsByEmail(email: String): Boolean = userDataComponent.findUserDataByEmail(email)

    fun getUserData(userUuid: UUID): UserData {
        return userDataComponent.getUserData(userUuid)
    }

    fun getUserProfileInfo(userUuid: UUID): UserProfileResponse =
        UserProfileResponse(
            userDataComponent.getUserData(userUuid),
            bookpointClient.getBookingInfo(userUuid)
        )

    fun editProfile(userUuid: UUID, request: ChangeProfileDataRequest) {
        val photoUrl = request.profileImage?.let {
            imageService.uploadImage(request.profileImage.inputStream, userUuid.toString())
        }

        val passwordSalt = if (request.password != null) encryptionService.generateSalt() else null
        val hashedPassword = request.password?.let { encryptionService.encrypt(it, passwordSalt!!) }

        request.toChangedProfileData(hashedPassword, passwordSalt, photoUrl).let {
            userDataComponent.editProfile(userUuid, it)
        }
    }

    private fun notifyUser(data: UserPrivateData) = notificationService.sendNotification(
        RegistrationNotification(
            email = data.email,
            eventType = NotificationType.GREETING,
            firstName = data.firstName,
        )
    )
}
