package com.rprikhodko.habrareader.categories.companies.domain

import com.rprihodko.habrareader.common.dto.CompaniesPage
import retrofit2.Response

interface GetCompaniesUseCase {
    suspend operator fun invoke(page: Int): Response<CompaniesPage>
}