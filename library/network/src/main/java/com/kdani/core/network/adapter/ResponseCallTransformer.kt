package com.kdani.core.network.adapter

import com.kdani.core.network.NetworkResponse
import com.kdani.core.network.RetrofitStarter
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

/**
 * Response call transformer to translate everything to [NetworkResponse]
 * Applies Global timeout for all API calls to be 5 seconds [GLOBAL_TIMEOUT]
 */
internal class ResponseCallTransformer<T : Any>(
    private val delegate: Call<T>,
) : Call<NetworkResponse<T>> {
    override fun clone(): Call<NetworkResponse<T>> = ResponseCallTransformer(delegate.clone())

    override fun execute(): Response<NetworkResponse<T>> {
        throw UnsupportedOperationException("ResponseCallTransformer doesn't support execute")
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout =
        Timeout().timeout(RetrofitStarter.GLOBAL_TIMEOUT, TimeUnit.MILLISECONDS)

    override fun enqueue(callback: Callback<NetworkResponse<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@ResponseCallTransformer,
                            Response.success(NetworkResponse.Success(body))
                        )
                    } else {
                        callback.onResponse(
                            this@ResponseCallTransformer, Response.success(NetworkResponse.Empty)
                        )
                    }
                } else {
                    val error = response.errorBody()?.string()
                    callback.onResponse(
                        this@ResponseCallTransformer,
                        Response.success(NetworkResponse.ApiError(Exception("Server error"), error))
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@ResponseCallTransformer,
                    Response.success(/* body = */ NetworkResponse.ApiError(t))
                )
            }

        })
    }
}