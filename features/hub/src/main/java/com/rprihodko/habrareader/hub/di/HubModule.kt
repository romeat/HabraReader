package com.rprihodko.habrareader.hub.di

import com.rprihodko.habrareader.common.di.HubCategory
import com.rprihodko.habrareader.common.interfaces.GetPostsByCategoryUseCase
import com.rprihodko.habrareader.hub.domain.GetHubPostsUseCase
import com.rprihodko.habrareader.hub.domain.GetHubProfileUseCase
import com.rprihodko.habrareader.hub.domain.GetHubProfileUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HubModule {

    @Binds
    abstract fun bindHubProfileUseCase(useCaseImpl: GetHubProfileUseCaseImpl) : GetHubProfileUseCase

    @Binds
    @HubCategory
    abstract fun bindHubPostsUseCase(useCaseImpl: GetHubPostsUseCase) : GetPostsByCategoryUseCase

}