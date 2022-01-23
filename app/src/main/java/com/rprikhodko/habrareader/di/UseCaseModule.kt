package com.rprikhodko.habrareader.di

import com.rprikhodko.habrareader.home.news.domain.GetNewsUseCase
import com.rprikhodko.habrareader.home.news.domain.GetNewsUseCaseImpl
import com.rprikhodko.habrareader.network.HabrRemoteData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindNewsUseCase(useCaseImpl: GetNewsUseCaseImpl) : GetNewsUseCase
}