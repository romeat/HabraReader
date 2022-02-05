package com.rprikhodko.habrareader.di

import com.rprikhodko.habrareader.categories.authors.domain.GetAuthorsUseCase
import com.rprikhodko.habrareader.categories.authors.domain.GetAuthorsUseCaseImpl
import com.rprikhodko.habrareader.categories.companies.domain.GetCompaniesUseCase
import com.rprikhodko.habrareader.categories.companies.domain.GetCompaniesUseCaseImpl
import com.rprikhodko.habrareader.categories.hubs.domain.GetHubsUseCase
import com.rprikhodko.habrareader.categories.hubs.domain.GetHubsUseCaseImpl
import com.rprikhodko.habrareader.home.news.domain.GetNewsUseCase
import com.rprikhodko.habrareader.home.news.domain.GetNewsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindNewsUseCase(useCaseImpl: GetNewsUseCaseImpl) : GetNewsUseCase


    /* Author related bindings */

    @Binds
    abstract fun bindAuthorsUseCase(useCaseImpl: GetAuthorsUseCaseImpl) : GetAuthorsUseCase


    /* Hub related bindings */

    @Binds
    abstract fun bindHubsUseCase(useCaseImpl: GetHubsUseCaseImpl) : GetHubsUseCase


    /* Company related bindings */

    @Binds
    abstract fun bindCompaniesUseCase(useCaseImpl: GetCompaniesUseCaseImpl) : GetCompaniesUseCase

}