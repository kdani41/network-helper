package com.kdani.core.network

import com.kdani.core.network.adapter.ResponseFactory
import com.kdani.core.network.interceptors.ConnectionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitStarter {

    internal var GLOBAL_TIMEOUT = 5000L

    /**
     * Builds a retrofit instance for the provided baseUrl.
     */
    fun build(baseUrl: String, timeoutInMillis: Long = GLOBAL_TIMEOUT): Retrofit {
        require(baseUrl.isNotEmpty()) {
            "base url can't be empty!!"
        }
        GLOBAL_TIMEOUT = timeoutInMillis
        val builder = OkHttpClient().newBuilder().apply {
            addInterceptor(ConnectionInterceptor())
        }
        return Retrofit.Builder().apply {
            baseUrl(baseUrl)
            client(builder.build())
            addConverterFactory(MoshiConverterFactory.create())
            addCallAdapterFactory(ResponseFactory)
        }.build()
    }
}