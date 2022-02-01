package com.rprikhodko.habrareader.company.domain

import com.rprikhodko.habrareader.company.data.CompanyProfile
import retrofit2.Response

interface GetCompanyProfileUseCase {
    suspend operator fun invoke(companyAlias: String): Response<CompanyProfile>
}