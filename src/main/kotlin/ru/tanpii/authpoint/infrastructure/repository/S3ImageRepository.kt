package ru.tanpii.authpoint.infrastructure.repository

import org.springframework.stereotype.Component
import ru.tanpii.authpoint.domain.repository.ImageRepository
import ru.tanpii.authpoint.support.properties.S3Properties
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.InputStream

@Component
class S3ImageRepository(
    private val s3Client: S3Client,
    private val properties: S3Properties
) : ImageRepository {

    override fun uploadImage(file: InputStream, name: String): String {
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(properties.bucketName)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .key(name)
                .build(),
            RequestBody.fromBytes(file.readBytes())
        )

        return "https://storage.yandexcloud.net/${properties.bucketName}/$name"
    }

    override fun getImageUrl(name: String): String {
        return "https://storage.yandexcloud.net/${properties.bucketName}/$name"
    }
}
