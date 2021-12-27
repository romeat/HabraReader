package com.rprikhodko.habrareader.data.dto.user

import com.google.gson.annotations.SerializedName

data class UserSmallInfo(
    val id: Int,
    val alias: String,
    @SerializedName("fullname") val fullName: String,
    val avatarUrl: String
)
