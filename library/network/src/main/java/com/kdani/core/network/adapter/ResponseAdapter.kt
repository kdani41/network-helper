package com.kdani.core.network.adapter

import com.kdani.core.network.NetworkResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Response adapter to translate everything to [NetworkResponse]
 */
internal class ResponseAdapter<T : Any>(private val successType: Type) :
    CallAdapter<T, Call<NetworkResponse<T>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<NetworkResponse<T>> = ResponseCallTransformer(call)
}
