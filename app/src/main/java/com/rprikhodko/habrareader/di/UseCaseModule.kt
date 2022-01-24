package com.rprikhodko.habrareader.di

import com.rprikhodko.habrareader.categories.authors.domain.GetAuthorsUseCase
import com.rprikhodko.habrareader.categories.authors.domain.GetAuthorsUseCaseImpl
import com.rprikhodko.habrareader.categories.companies.domain.GetCompaniesUseCase
import com.rprikhodko.habrareader.categories.companies.domain.GetCompaniesUseCaseImpl
import com.rprikhodko.habrareader.categories.hubs.domain.GetHubsUseCase
import com.rprikhodko.habrareader.categories.hubs.domain.GetHubsUseCaseImpl
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

    @Binds
    abstract fun bindAuthorsUseCase(useCaseImpl: GetAuthorsUseCaseImpl) : GetAuthorsUseCase

    @Binds
    abstract fun bindHubsUseCase(useCaseImpl: GetHubsUseCaseImpl) : GetHubsUseCase

    @Binds
    abstract fun bindCompaniesUseCase(useCaseImpl: GetCompaniesUseCaseImpl) : GetCompaniesUseCase
}