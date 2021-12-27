package com.rprikhodko.habrareader.data.dto.hub

import com.google.gson.annotations.SerializedName

data class HubDetails (
    val alias: String,
    @SerializedName("titleHtml") val title: String,
    @SerializedName("descriptionHtml") val description: String,
    @SerializedName("fullDescriptionHtml") val fullDescription: String,
    val imageUrl: String,
    val statistics: HubStatistics,
    val keywords: List<String>,
)