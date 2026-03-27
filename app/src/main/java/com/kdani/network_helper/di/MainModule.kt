package com.kdani.network_helper.di

import com.kdani.core.network.RetrofitStarter
import com.kdani.network_helper.network.SampleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MainModule {
    private const val BASE_URL = "https://s3.amazonaws.com/sq-mobile-interview/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return RetrofitStarter.build(
            baseUrl = BASE_URL,
            additionalInterceptors = listOf(loggingInterceptor)
        )
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): SampleService = retrofit.create(SampleService::class.java)
}