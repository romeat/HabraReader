package com.rprikhodko.habrareader.data.dto.post

import com.google.gson.annotations.SerializedName
import com.rprikhodko.habrareader.data.dto.user.UserSmallInfo

data class Post(
    val id: Int,
    @SerializedName("titleHtml") val title: String,
    val postType: String,
    val author: UserSmallInfo
)
