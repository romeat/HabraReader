package com.rprikhodko.habrareader.data.dto.post

import com.google.gson.annotations.SerializedName
import com.rprikhodko.habrareader.data.dto.user.User
import com.rprikhodko.habrareader.data.dto.user.UserSmallInfo
import java.time.LocalDateTime

data class PostPreview(
    val id: Int,
    @SerializedName("titleHtml") val title: String,
    val postType: String,
    val author: UserSmallInfo,
    @SerializedName("statistics") val stats: PostStatistics,
    val timePublished: String
)
