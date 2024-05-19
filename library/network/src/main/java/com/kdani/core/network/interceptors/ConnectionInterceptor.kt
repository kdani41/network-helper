package com.kdani.core.network.interceptors

import com.kdani.core.network.exceptions.NoNetworkException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


private const val NO_NETWORK_HOST_NAME = "8.8.8.8"
private const val NO_NETWORK_HOST_PORT = 53
private const val TIMEOUT = 1500

/**
 * Checks if internet is present or not
 * [Source](https://stackoverflow.com/a/9570292)
 */
internal class ConnectionInterceptor : Interceptor {
    private fun isInternetAvailable(): Boolean {
        return try {
            val socket = Socket()
            val socketAddress = InetSocketAddress(NO_NETWORK_HOST_NAME, NO_NETWORK_HOST_PORT)
            socket.connect(socketAddress, TIMEOUT)
            socket.close()
            true
        } catch (io: IOException) {
            false
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isInternetAvailable()) {
            throw NoNetworkException()
        } else {
            chain.proceed(chain.request())
        }
    }
}