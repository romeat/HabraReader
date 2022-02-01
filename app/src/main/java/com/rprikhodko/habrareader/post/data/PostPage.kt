package com.rprikhodko.habrareader.post.data

import com.google.gson.annotations.SerializedName
import com.rprikhodko.habrareader.common.data.dto.PostStatistics
import com.rprikhodko.habrareader.common.data.dto.UserSmallInfo

data class PostPage(
    val id: Int,
    val timePublished: String,
    @SerializedName("titleHtml") val title: String,
    val statistics: PostStatistics,
    val author: UserSmallInfo,
    @SerializedName("textHtml") val content: String,
)
