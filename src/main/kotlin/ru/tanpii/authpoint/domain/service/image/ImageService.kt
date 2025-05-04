package ru.tanpii.authpoint.domain.service.image

import org.springframework.stereotype.Service
import ru.tanpii.authpoint.domain.repository.ImageRepository
import java.io.InputStream

@Service
class ImageService(
    private val imageRepository: ImageRepository,
) {
    fun uploadImage(file: InputStream, name: String): String = imageRepository.uploadImage(file, name)
}