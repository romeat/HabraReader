package com.rprikhodko.habrareader.company.domain

import com.rprihodko.habrareader.common.dto.CompanyProfile
import retrofit2.Response

interface GetCompanyProfileUseCase {
    suspend operator fun invoke(companyAlias: String): Response<CompanyProfile>
}