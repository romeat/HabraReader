package com.rprihodko.habrareader.common.dto

data class CompaniesPage(
    val pagesCount: Int,
    val companyIds: List<String>,
    val companyRefs: Map<String, CompanyPreview>
)
