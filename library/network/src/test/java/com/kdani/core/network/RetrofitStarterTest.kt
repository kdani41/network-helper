package com.kdani.core.network

import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitStarterTest {

    private val baseUrl = "https://example.com/"

    @Test
    fun `build with no factory defaults to Moshi`() {
        val retrofit = RetrofitStarter.build(baseUrl = baseUrl)
        assertNotNull(retrofit)
    }

    @Test
    fun `build with explicit MoshiConverterFactory succeeds`() {
        val retrofit = RetrofitStarter.build(
            baseUrl = baseUrl,
            convertorFactory = MoshiConverterFactory.create()
        )
        assertNotNull(retrofit)
    }

    @Test
    fun `build with GsonConverterFactory succeeds`() {
        val retrofit = RetrofitStarter.build(
            baseUrl = baseUrl,
            convertorFactory = GsonConverterFactory.create()
        )
        assertNotNull(retrofit)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `build with empty baseUrl throws`() {
        RetrofitStarter.build(baseUrl = "")
    }
}
