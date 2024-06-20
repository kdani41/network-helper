package com.kdani.ktor.helpers

import com.kdani.ktor.exceptions.NoNetworkException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.util.AttributeKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class NetworkInterceptor {

    private suspend fun isInternetAvailable(): Boolean = withContext(Dispatchers.IO) {
        try {
            val socket = aSocket(SelectorManager(Dispatchers.IO)).tcp().connect(
                InetSocketAddress(NO_NETWORK_HOST_NAME, NO_NETWORK_HOST_PORT)
            ) {
                socketTimeout = TIMEOUT
            }
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    companion object Feature : HttpClientPlugin<Nothing, NetworkInterceptor> {

        private const val NO_NETWORK_HOST_NAME = "8.8.8.8"
        private const val NO_NETWORK_HOST_PORT = 53
        private const val TIMEOUT = 1500L

        override val key = AttributeKey<NetworkInterceptor>("NetworkInterceptor")

        override fun prepare(block: Nothing.() -> Unit) = NetworkInterceptor()

        override fun install(plugin: NetworkInterceptor, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                if (!plugin.isInternetAvailable()) {
                    throw NoNetworkException()
                }
            }
        }
    }

}