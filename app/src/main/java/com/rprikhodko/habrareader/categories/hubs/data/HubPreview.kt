package com.rprikhodko.habrareader.categories.hubs.data

import com.google.gson.annotations.SerializedName
import com.rprikhodko.habrareader.common.data.dto.HubStatistics

data class HubPreview(
    val id: Int,
    val alias: String,
    @SerializedName("titleHtml") val title: String,
    val imageUrl: String,
    @SerializedName("descriptionHtml") val description: String,
    val statistics: HubStatistics,
)
