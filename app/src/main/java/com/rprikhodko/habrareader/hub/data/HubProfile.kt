package com.rprikhodko.habrareader.hub.data

import com.rprikhodko.habrareader.common.data.dto.HubStatistics

data class HubProfile(
    val alias: String,
    val titleHtml: String,
    val descriptionHtml: String,
    val statistics: HubStatistics,
    val imageUrl: String?
)
