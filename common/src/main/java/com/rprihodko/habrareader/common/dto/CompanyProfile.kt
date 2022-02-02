package com.rprihodko.habrareader.common.dto

import com.rprihodko.habrareader.common.dto.CompanyStatistics

data class CompanyProfile(
    val alias: String,
    val imageUrl: String?,
    val titleHtml: String,
    val descriptionHtml: String?,
    val siteUrl: String?,
    val staffNumber: String?,
    val statistics: CompanyStatistics
)
