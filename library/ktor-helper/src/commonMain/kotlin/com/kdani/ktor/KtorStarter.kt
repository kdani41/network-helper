package com.kdani.ktor

import com.kdani.ktor.helpers.NetworkInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

typealias HttpClientInterceptor = (HttpClientConfig<*>.() -> Unit)

object KtorStarter {

    fun buildClient(
        baseUrl: String,
        timeoutInMillis: Long = 5000L,
        interceptors: List<HttpClientInterceptor> = emptyList()
    ) = HttpClient(CIO) {
        require(baseUrl.isNotEmpty()) {
            "base url=$baseUrl shouldn't be empty"
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        defaultRequest {
            url.takeFrom(baseUrl)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = timeoutInMillis
            connectTimeoutMillis = timeoutInMillis
            socketTimeoutMillis = timeoutInMillis
        }
        install(NetworkInterceptor)
        interceptors.forEach(::apply)
    }
}
