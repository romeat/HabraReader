package com.rprikhodko.habrareader.company.domain

import com.rprikhodko.habrareader.company.data.CompanyProfile
import com.rprikhodko.habrareader.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetCompanyProfileUseCaseImpl @Inject constructor(
    private val remoteData : HabrRemoteData
) : GetCompanyProfileUseCase {
    override suspend fun invoke(companyAlias: String): Response<CompanyProfile> = remoteData.getCompanyProfile(companyAlias)
}