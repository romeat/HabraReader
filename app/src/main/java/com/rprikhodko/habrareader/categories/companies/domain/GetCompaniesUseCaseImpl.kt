package com.rprikhodko.habrareader.categories.companies.domain

import com.rprikhodko.habrareader.categories.companies.data.CompaniesPage
import com.rprikhodko.habrareader.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetCompaniesUseCaseImpl @Inject constructor(
    private val remoteData: HabrRemoteData
) : GetCompaniesUseCase {
    override suspend fun invoke(page: Int): Response<CompaniesPage> = remoteData.getCompanies(page)
}