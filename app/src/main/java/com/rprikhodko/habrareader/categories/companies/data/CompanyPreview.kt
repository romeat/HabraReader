package com.rprikhodko.habrareader.categories.companies.data

import com.google.gson.annotations.SerializedName
import com.rprikhodko.habrareader.common.data.dto.CompanyStatistics

data class CompanyPreview(
    val id: Int,
    val alias: String,
    @SerializedName("titleHtml") val title: String,
    @SerializedName("descriptionHtml") val description: String,
    val statistics: CompanyStatistics,
)
