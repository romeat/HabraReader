package com.rprikhodko.habrareader.company.data

import com.rprikhodko.habrareader.common.data.dto.CompanyStatistics

data class CompanyProfile(
    val alias: String,
    val imageUrl: String?,
    val titleHtml: String,
    val descriptionHtml: String?,
    val siteUrl: String?,
    val staffNumber: String?,
    val statistics: CompanyStatistics
)
