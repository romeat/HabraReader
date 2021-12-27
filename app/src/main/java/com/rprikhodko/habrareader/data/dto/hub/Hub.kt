package com.rprikhodko.habrareader.data.dto.hub

import com.google.gson.annotations.SerializedName

data class Hub(
    val id: Int,
    val alias: String,
    @SerializedName("titleHtml") val title: String,
    @SerializedName("descriptionHtml") val description: String,
    val imageUrl: String,
    val statistics: HubStatistics
)
