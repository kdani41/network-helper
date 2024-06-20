@file:OptIn(InternalSerializationApi::class)

import com.kdani.ktor.helpers.NetworkResponse
import com.kdani.ktor.RequestOptions
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

suspend inline fun <reified T : Any> HttpClient.safeRequest(
    requestBuilder: HttpRequestBuilder,
    requestOptions: RequestOptions = RequestOptions()
): NetworkResponse<T> {
    return try {
        // Add headers to the request
        requestOptions.headers.forEach { (key, value) ->
            requestBuilder.headers.append(key, value)
        }

        // Add query parameters to the request
        requestOptions.queryParams.forEach { (key, value) ->
            requestBuilder.url.parameters.append(key, value)
        }

        val response = this.request(requestBuilder)
        val responseBody: String = response.bodyAsText()
        when (response.status) {
            HttpStatusCode.NoContent -> NetworkResponse.Empty
            HttpStatusCode.OK -> {
                try {
                    val data = Json.decodeFromString(T::class.serializer(), responseBody)
                    NetworkResponse.Success(data)
                } catch (e: SerializationException) {
                    NetworkResponse.ApiError(e)
                }
            }

            else -> NetworkResponse.ApiError(Exception("HTTP error with status code: ${response.status.value}"))
        }
    } catch (e: Exception) {
        NetworkResponse.ApiError(e)
    }
}

suspend inline fun <reified T : Any> HttpClient.safeCall(
    url: String,
    httpMethod: HttpMethod,
    requestOptions: RequestOptions = RequestOptions(),
): NetworkResponse<T> {
    val request = HttpRequestBuilder().apply {
        url(url)
        method = httpMethod
        requestOptions.body?.let {
            setBody(it)
        }
    }

    return safeRequest(request, requestOptions)
}