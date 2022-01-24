package com.rprikhodko.habrareader.categories.companies.domain

import com.rprikhodko.habrareader.categories.companies.data.CompaniesPage
import retrofit2.Response

interface GetCompaniesUseCase {
    suspend operator fun invoke(page: Int): Response<CompaniesPage>
}