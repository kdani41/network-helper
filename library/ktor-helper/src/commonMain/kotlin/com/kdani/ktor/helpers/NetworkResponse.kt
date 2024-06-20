package com.kdani.ktor.helpers

/**
 * Response representing network payload.
 */
sealed interface NetworkResponse<out T : Any> {

    /**
     * Represents success response from API.
     */
    data class Success<T : Any>(val response: T) : NetworkResponse<T>

    /**
     * Represents API error.
     */
    data class ApiError(val throwable: Throwable) : NetworkResponse<Nothing>

    /**
     * Represents empty screen when data is not present.
     */
    data object Empty : NetworkResponse<Nothing>
}