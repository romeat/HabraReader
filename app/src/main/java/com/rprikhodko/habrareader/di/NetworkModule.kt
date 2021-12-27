package com.rprikhodko.habrareader.di

import com.rprikhodko.habrareader.HabrRemoteData
import com.rprikhodko.habrareader.MainService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetwork() : Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://habr.com/kek/v2/")
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val url = chain
                        .request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("fl", "ru")
                        .addQueryParameter("hl", "ru")
                        .build()
                    chain.proceed(chain.request().newBuilder().url(url).build())
                }
                .build()
        )
        .build()

    @Provides
    @Singleton
    fun provideMainService(retrofit : Retrofit) : MainService = retrofit.create(MainService::class.java)

    @Provides
    @Singleton
    fun provideMainRemoteData(mainService : MainService) : HabrRemoteData = HabrRemoteData(mainService)
}
