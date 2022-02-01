package com.rprikhodko.habrareader.categories.authors.data

import com.google.gson.annotations.SerializedName
import com.rprikhodko.habrareader.common.data.dto.UserScore

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
