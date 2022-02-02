package com.rprihodko.habrareader.common.dto

import com.google.gson.annotations.SerializedName
import com.rprihodko.habrareader.common.dto.HubStatistics

data class HubPreview(
    val id: Int,
    val alias: String,
    @SerializedName("titleHtml") val title: String,
    val imageUrl: String,
    @SerializedName("descriptionHtml") val description: String,
    val statistics: HubStatistics,
)
