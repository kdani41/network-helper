package com.kdani.network_helper.di

import com.kdani.core.network.RetrofitStarter
import com.kdani.ktor.KtorStarter
import com.kdani.network_helper.network.SampleKtorService
import com.kdani.network_helper.network.SampleKtorServiceImpl
import com.kdani.network_helper.network.SampleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MainModule {
    private const val BASE_URL = "https://s3.amazonaws.com/sq-mobile-interview/"

    @Singleton
    @Provides
    fun provideRetrofit() = RetrofitStarter.build(BASE_URL)

    @Singleton
    @Provides
    fun provideKtor(): HttpClient = KtorStarter.buildClient(BASE_URL)

    @Singleton
    @Provides
    fun provideKtorApi(impl: SampleKtorServiceImpl): SampleKtorService = impl

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): SampleService = retrofit.create(SampleService::class.java)
}