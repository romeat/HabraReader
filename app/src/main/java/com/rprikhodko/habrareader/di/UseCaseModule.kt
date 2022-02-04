package com.rprikhodko.habrareader.di

import com.rprihodko.habrareader.author.domain.GetAuthorPostsUseCase
import com.rprihodko.habrareader.author.domain.GetAuthorProfileUseCase
import com.rprihodko.habrareader.author.domain.GetAuthorProfileUseCaseImpl
import com.rprikhodko.habrareader.categories.authors.domain.GetAuthorsUseCase
import com.rprikhodko.habrareader.categories.authors.domain.GetAuthorsUseCaseImpl
import com.rprikhodko.habrareader.categories.companies.domain.GetCompaniesUseCase
import com.rprikhodko.habrareader.categories.companies.domain.GetCompaniesUseCaseImpl
import com.rprikhodko.habrareader.categories.hubs.domain.GetHubsUseCase
import com.rprikhodko.habrareader.categories.hubs.domain.GetHubsUseCaseImpl
import com.rprihodko.habrareader.common.interfaces.GetPostsByCategoryUseCase
import com.rprihodko.habrareader.company.domain.GetCompanyPostsUseCase
import com.rprihodko.habrareader.company.domain.GetCompanyProfileUseCase
import com.rprihodko.habrareader.company.domain.GetCompanyProfileUseCaseImpl
import com.rprikhodko.habrareader.home.news.domain.GetNewsUseCase
import com.rprikhodko.habrareader.home.news.domain.GetNewsUseCaseImpl
import com.rprihodko.habrareader.hub.domain.GetHubPostsUseCase
import com.rprihodko.habrareader.hub.domain.GetHubProfileUseCase
import com.rprihodko.habrareader.hub.domain.GetHubProfileUseCaseImpl
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