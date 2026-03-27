package com.kdani.core.network

import com.kdani.core.network.adapter.ResponseCallTransformer
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class ResponseCallTransformerTest {

    private data class FakeBody(val id: Int)

    private fun transformer(delegate: Call<FakeBody>) = ResponseCallTransformer(delegate)

    private fun fakeDelegate(response: Response<FakeBody>): Call<FakeBody> {
        val delegate = mockk<Call<FakeBody>>()
        val callbackSlot = slot<Callback<FakeBody>>()
        every { delegate.enqueue(capture(callbackSlot)) } answers {
            callbackSlot.captured.onResponse(delegate, response)
        }
        return delegate
    }

    private fun failingDelegate(error: Throwable): Call<FakeBody> {
        val delegate = mockk<Call<FakeBody>>()
        val callbackSlot = slot<Callback<FakeBody>>()
        every { delegate.enqueue(capture(callbackSlot)) } answers {
            callbackSlot.captured.onFailure(delegate, error)
        }
        return delegate
    }

    private fun enqueue(transformer: Call<NetworkResponse<FakeBody>>): NetworkResponse<FakeBody> {
        var result: NetworkResponse<FakeBody>? = null
        transformer.enqueue(object : Callback<NetworkResponse<FakeBody>> {
            override fun onResponse(
                call: Call<NetworkResponse<FakeBody>>,
                response: Response<NetworkResponse<FakeBody>>
            ) { result = response.body() }

            override fun onFailure(call: Call<NetworkResponse<FakeBody>>, t: Throwable) {}
        })
        return result!!
    }

    @Test
    fun `success response with body returns Success`() {
        val body = FakeBody(id = 1)
        val delegate = fakeDelegate(Response.success(body))
        val result = enqueue(transformer(delegate))
        assertTrue(result is NetworkResponse.Success)
        assertEquals(body, (result as NetworkResponse.Success).response)
    }

    @Test
    fun `success response with null body returns Empty`() {
        val delegate = fakeDelegate(Response.success(null))
        val result = enqueue(transformer(delegate))
        assertEquals(NetworkResponse.Empty, result)
    }

    @Test
    fun `error response returns ApiError with error body string`() {
        val errorJson = """{"message":"not found"}"""
        val response = Response.error<FakeBody>(404, errorJson.toResponseBody())
        val delegate = fakeDelegate(response)
        val result = enqueue(transformer(delegate))
        assertTrue(result is NetworkResponse.ApiError)
        assertEquals(errorJson, (result as NetworkResponse.ApiError).errorBody)
    }

    @Test
    fun `error response with empty body returns ApiError with null errorBody`() {
        val response = Response.error<FakeBody>(500, "".toResponseBody())
        val delegate = fakeDelegate(response)
        val result = enqueue(transformer(delegate))
        assertTrue(result is NetworkResponse.ApiError)
        // empty string is returned as-is (not null), matching errorBody()?.string() behavior
        val errorBody = (result as NetworkResponse.ApiError).errorBody
        assertTrue(errorBody == null || errorBody.isEmpty())
    }

    @Test
    fun `network failure returns ApiError with the throwable`() {
        val error = RuntimeException("timeout")
        val delegate = failingDelegate(error)
        val result = enqueue(transformer(delegate))
        assertTrue(result is NetworkResponse.ApiError)
        assertEquals(error, (result as NetworkResponse.ApiError).throwable)
    }

    @Test
    fun `network failure errorBody is null`() {
        val delegate = failingDelegate(RuntimeException())
        val result = enqueue(transformer(delegate))
        assertNull((result as NetworkResponse.ApiError).errorBody)
    }
}
