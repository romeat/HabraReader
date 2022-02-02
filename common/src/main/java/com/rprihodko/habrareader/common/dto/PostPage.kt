package com.rprihodko.habrareader.common.dto

import com.google.gson.annotations.SerializedName
import com.rprihodko.habrareader.common.dto.PostStatistics
import com.rprihodko.habrareader.common.dto.UserSmallInfo

data class PostPage(
    val id: Int,
    val timePublished: String,
    @SerializedName("titleHtml") val title: String,
    val statistics: PostStatistics,
    val author: UserSmallInfo,
    @SerializedName("textHtml") val content: String,
)
