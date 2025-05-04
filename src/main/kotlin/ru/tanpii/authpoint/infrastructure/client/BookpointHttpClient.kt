package ru.tanpii.authpoint.infrastructure.client

import org.springframework.stereotype.Component
import ru.tanpii.authpoint.domain.client.BookpointClient
import ru.tanpii.authpoint.domain.model.dto.BookingInfo
import ru.tanpii.authpoint.support.factory.WebClientFactory
import java.util.*

@Component
class BookpointHttpClient(
    webClientFactory: WebClientFactory
) : BookpointClient {

    val webClient by lazy {
        webClientFactory.createWebClient("bookpoint")
    }

    override fun getBookingInfo(userId: UUID) = webClient.get()
        .uri("/api/v1/internal/booking/{userId}", userId)
        .retrieve()
        .bodyToMono(BookingInfo::class.java)
        .block()
}
