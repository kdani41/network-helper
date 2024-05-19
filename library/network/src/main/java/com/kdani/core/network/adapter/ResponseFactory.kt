package com.kdani.core.network.adapter

import com.kdani.core.network.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Response factory to translate everything to [NetworkResponse]
 */
internal object ResponseFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        val responseType = (returnType as? ParameterizedType)?.let { getParameterUpperBound(0, it) }
        return if (responseType is ParameterizedType && getRawType(responseType) == NetworkResponse::class.java) {
            val bodyType = getParameterUpperBound(0, responseType)
            ResponseAdapter<Any>(bodyType)
        } else {
            Timber.e("return type must be parameterize type of NetworkResponse<Foo>")
            null
        }
    }
}