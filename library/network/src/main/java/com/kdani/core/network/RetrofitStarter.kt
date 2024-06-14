package com.kdani.core.network

import com.kdani.core.network.adapter.ResponseFactory
import com.kdani.core.network.interceptors.ConnectionInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitStarter {

    internal var GLOBAL_TIMEOUT = 5000L
        private set

    internal val client by lazy {
        OkHttpClient().newBuilder()
    }

    /**
     * Builds a retrofit instance for the provided baseUrl.
     */
    fun build(
        baseUrl: String,
        timeoutInMillis: Long = GLOBAL_TIMEOUT,
        additionalInterceptors: List<Interceptor> = emptyList(),
        convertorFactory: MoshiConverterFactory? = null
    ): Retrofit {
        require(baseUrl.isNotEmpty()) {
            "base url can't be empty!!"
        }
        GLOBAL_TIMEOUT = timeoutInMillis
        val builder = client.apply {
            addInterceptor(ConnectionInterceptor())
            additionalInterceptors.forEach(::addInterceptor)
        }
        return Retrofit.Builder().apply {
            baseUrl(baseUrl)
            client(builder.build())
            addConverterFactory(convertorFactory ?: MoshiConverterFactory.create())
            addCallAdapterFactory(ResponseFactory)
        }.build()
    }
}