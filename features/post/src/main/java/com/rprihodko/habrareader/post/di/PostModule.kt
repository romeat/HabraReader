package com.rprihodko.habrareader.post.di

import com.rprihodko.habrareader.common.interfaces.GetPostsByCategoryUseCase
import com.rprihodko.habrareader.post.domain.GetPostUseCase
import com.rprihodko.habrareader.post.domain.GetPostUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PostModule {

    @Binds
    abstract fun bindPostUseCase(useCaseImpl: GetPostUseCaseImpl) : GetPostUseCase

}