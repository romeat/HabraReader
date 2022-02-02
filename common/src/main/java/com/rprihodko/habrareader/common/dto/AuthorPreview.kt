package com.rprihodko.habrareader.common.dto

import com.google.gson.annotations.SerializedName
import com.rprihodko.habrareader.common.dto.UserScore

data class AuthorPreview(
    val id: Int,
    val rating: Double,
    val alias: String,
    @SerializedName("fullname")
    val fullName: String?,
    val avatarUrl: String?,
    val speciality: String?,
    val scoreStats: UserScore,
)
