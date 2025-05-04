package ru.tanpii.authpoint.domain.service.user.component

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.tanpii.authpoint.domain.model.dto.ChangedProfileData
import ru.tanpii.authpoint.domain.model.dto.UserData
import ru.tanpii.authpoint.domain.model.dto.UserPrivateData
import ru.tanpii.authpoint.domain.model.entity.UserDataEntity
import ru.tanpii.authpoint.domain.model.exception.UserNotFoundException
import ru.tanpii.authpoint.domain.repository.UserDataRepository
import ru.tanpii.authpoint.support.mapper.toUserData
import ru.tanpii.authpoint.support.mapper.toUserPrivateData
import java.util.*

@Component
class UserDataComponent(
    private val userDataRepository: UserDataRepository
) {

    @Transactional
    fun createUserData(userData: UserPrivateData) =
        userDataRepository.addUser(
            UserDataEntity(
                uuid = userData.uuid,
                email = userData.email,
                password = userData.password,
                salt = userData.salt,
                firstName = userData.firstName,
                lastName = userData.lastName,
                birthDate = userData.birthDate,
                photoUrl = userData.photoUrl
            )
        )

    @Transactional(readOnly = true)
    fun findUserDataByEmail(email: String): Boolean = userDataRepository.existsByEmail(email)

    @Transactional
    fun getUserData(userUuid: UUID): UserData {
        val userDataEntity =
            userDataRepository.findByUuid(userUuid) ?: throw UserNotFoundException("User with uuid=$userUuid not found")
        return userDataEntity.toUserData()
    }

    @Transactional(readOnly = true)
    fun getUserPrivateData(email: String): UserPrivateData =
        userDataRepository.findByEmail(email)?.toUserPrivateData()
            ?: throw UserNotFoundException("User with email=$email not found")

    @Transactional
    fun editProfile(userUuid: UUID, data: ChangedProfileData) {
        val user = userDataRepository.findByUuid(userUuid)
            ?: throw UserNotFoundException("User with uuid=$userUuid not found")

        userDataRepository.save(
            UserDataEntity(
                uuid = user.uuid,
                email = data.email ?: user.email,
                salt = data.salt ?: user.salt,
                password = data.password ?: user.password,
                firstName = data.firstName ?: user.firstName,
                lastName = data.lastName ?: user.lastName,
                birthDate = data.birthDate ?: user.birthDate,
                photoUrl = data.photoUrl ?: user.photoUrl
            )
        )
    }

    @Transactional(readOnly = true)
    fun getUserEmails() = userDataRepository.getUserEmails()
}
