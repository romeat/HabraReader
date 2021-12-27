package com.rprikhodko.habrareader.data.dto.post

import com.google.gson.annotations.SerializedName
import com.rprikhodko.habrareader.data.dto.user.User
import com.rprikhodko.habrareader.data.dto.user.UserSmallInfo

data class PostPreview(
    val id: Int,
    @SerializedName("titleHtml") val title: String,
    val postType: String,
    val author: UserSmallInfo,
    val stats: PostStatistics,
)
