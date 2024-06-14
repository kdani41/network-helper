package com.kdani.core.network

import okhttp3.Interceptor
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class RetrofitStarterTest {

    private lateinit var sut: RetrofitStarter

    @Before
    fun setUp() {
        sut = RetrofitStarter
    }

    @Test
    fun `test base url`() {
        val baseUrl = "https://google.com/"
        val instance = sut.build(baseUrl)
        Assert.assertEquals(baseUrl, instance.baseUrl().toString())
    }

    @Test
    fun `test interceptors`() {
        val baseUrl = "https://google.com/"
        val dummyIntercept = Interceptor { chain -> chain.proceed(chain.request()) }
        sut.build(baseUrl, additionalInterceptors = listOf(dummyIntercept))

        Assert.assertEquals(2, sut.client.interceptors().size)
    }
}