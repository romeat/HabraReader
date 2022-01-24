package com.rprikhodko.habrareader.categories.companies.data

data class CompaniesPage(
    val pagesCount: Int,
    val companyIds: List<String>,
    val companyRefs: Map<String, CompanyPreview>
)
