package com.rprihodko.habrareader.common.dto

import com.rprihodko.habrareader.common.dto.HubStatistics

data class HubProfile(
    val alias: String,
    val titleHtml: String,
    val descriptionHtml: String,
    val statistics: HubStatistics,
    val imageUrl: String?
)
