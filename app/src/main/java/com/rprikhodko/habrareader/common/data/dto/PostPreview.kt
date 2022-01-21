package com.rprikhodko.habrareader.common.data.dto

import com.google.gson.annotations.SerializedName

data class PostPreview(
    val id: Int,
    @SerializedName("titleHtml") val title: String,
    val postType: String,
    val author: UserSmallInfo,
    @SerializedName("statistics") val stats: PostStatistics,
    val timePublished: String
)
