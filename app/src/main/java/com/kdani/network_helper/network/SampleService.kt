package com.kdani.network_helper.network

import com.kdani.core.network.NetworkResponse
import retrofit2.http.GET

internal interface SampleService {

    @GET("employees.json")
    suspend fun fetchData(): NetworkResponse<Employees>
}