package com.rprihodko.habrareader.author.di

import com.rprihodko.habrareader.author.domain.GetAuthorPostsUseCase
import com.rprihodko.habrareader.author.domain.GetAuthorProfileUseCase
import com.rprihodko.habrareader.author.domain.GetAuthorProfileUseCaseImpl
import com.rprihodko.habrareader.common.di.AuthorCategory
import com.rprihodko.habrareader.common.interfaces.GetPostsByCategoryUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthorModule {

    @Binds
    abstract fun bindAuthorProfileUseCase(useCaseImpl: GetAuthorProfileUseCaseImpl) : GetAuthorProfileUseCase

    @Binds
    @AuthorCategory
    abstract fun bindAuthorPostsUseCase(useCaseImpl: GetAuthorPostsUseCase) : GetPostsByCategoryUseCase

}