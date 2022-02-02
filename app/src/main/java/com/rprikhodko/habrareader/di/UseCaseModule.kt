package com.rprikhodko.habrareader.di

import com.rprikhodko.habrareader.author.domain.GetAuthorPostsUseCase
import com.rprikhodko.habrareader.author.domain.GetAuthorProfileUseCase
import com.rprikhodko.habrareader.author.domain.GetAuthorProfileUseCaseImpl
import com.rprikhodko.habrareader.categories.authors.domain.GetAuthorsUseCase
import com.rprikhodko.habrareader.categories.authors.domain.GetAuthorsUseCaseImpl
import com.rprikhodko.habrareader.categories.companies.domain.GetCompaniesUseCase
import com.rprikhodko.habrareader.categories.companies.domain.GetCompaniesUseCaseImpl
import com.rprikhodko.habrareader.categories.hubs.domain.GetHubsUseCase
import com.rprikhodko.habrareader.categories.hubs.domain.GetHubsUseCaseImpl
import com.rprikhodko.habrareader.common.interfaces.GetPostsByCategoryUseCase
import com.rprikhodko.habrareader.company.domain.GetCompanyPostsUseCase
import com.rprikhodko.habrareader.company.domain.GetCompanyProfileUseCase
import com.rprikhodko.habrareader.company.domain.GetCompanyProfileUseCaseImpl
import com.rprikhodko.habrareader.home.news.domain.GetNewsUseCase
import com.rprikhodko.habrareader.home.news.domain.GetNewsUseCaseImpl
import com.rprikhodko.habrareader.hub.domain.GetHubPostsUseCase
import com.rprikhodko.habrareader.hub.domain.GetHubProfileUseCase
import com.rprikhodko.habrareader.hub.domain.GetHubProfileUseCaseImpl
import com.rprihodko.habrareader.post.domain.GetPostUseCase
import com.rprihodko.habrareader.post.domain.GetPostUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindNewsUseCase(useCaseImpl: GetNewsUseCaseImpl) : GetNewsUseCase

    @Binds
    abstract fun bindPostUseCase(useCaseImpl: GetPostUseCaseImpl) : GetPostUseCase


    /* Author related bindings */

    @Binds
    abstract fun bindAuthorsUseCase(useCaseImpl: GetAuthorsUseCaseImpl) : GetAuthorsUseCase

    @Binds
    abstract fun bindAuthorProfileUseCase(useCaseImpl: GetAuthorProfileUseCaseImpl) : GetAuthorProfileUseCase

    @Binds
    @AuthorCategory
    abstract fun bindAuthorPostsUseCase(useCaseImpl: GetAuthorPostsUseCase) : GetPostsByCategoryUseCase


    /* Hub related bindings */

    @Binds
    abstract fun bindHubsUseCase(useCaseImpl: GetHubsUseCaseImpl) : GetHubsUseCase

    @Binds
    abstract fun bindHubProfileUseCase(useCaseImpl: GetHubProfileUseCaseImpl) : GetHubProfileUseCase

    @Binds
    @HubCategory
    abstract fun bindHubPostsUseCase(useCaseImpl: GetHubPostsUseCase) : GetPostsByCategoryUseCase


    /* Company related bindings */

    @Binds
    abstract fun bindCompaniesUseCase(useCaseImpl: GetCompaniesUseCaseImpl) : GetCompaniesUseCase

    @Binds
    abstract fun bindCompanyProfileUseCase(useCaseImpl: GetCompanyProfileUseCaseImpl) : GetCompanyProfileUseCase

    @Binds
    @CompanyCategory
    abstract fun bindCompanyPostsUseCase(useCaseImpl: GetCompanyPostsUseCase) : GetPostsByCategoryUseCase

}