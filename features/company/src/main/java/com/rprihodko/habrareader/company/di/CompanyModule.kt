package com.rprihodko.habrareader.company.di

import com.rprihodko.habrareader.common.di.CompanyCategory
import com.rprihodko.habrareader.common.interfaces.GetPostsByCategoryUseCase
import com.rprihodko.habrareader.company.domain.GetCompanyPostsUseCase
import com.rprihodko.habrareader.company.domain.GetCompanyProfileUseCase
import com.rprihodko.habrareader.company.domain.GetCompanyProfileUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CompanyModule {

    @Binds
    abstract fun bindCompanyProfileUseCase(useCaseImpl: GetCompanyProfileUseCaseImpl) : GetCompanyProfileUseCase

    @Binds
    @CompanyCategory
    abstract fun bindCompanyPostsUseCase(useCaseImpl: GetCompanyPostsUseCase) : GetPostsByCategoryUseCase

}