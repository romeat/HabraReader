package com.rprikhodko.habrareader.common.data.dto

import com.google.gson.annotations.SerializedName

data class UserSmallInfo(
    val id: Int,
    val alias: String,
    @SerializedName("fullname") val fullName: String?,
    val avatarUrl: String?
)
