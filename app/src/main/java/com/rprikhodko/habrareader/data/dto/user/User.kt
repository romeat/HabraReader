package com.rprikhodko.habrareader.data.dto.user

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val rating: Double,
    val alias: String,
    @SerializedName("fullname") val fullName: String,
    val avatarUrl: String,
    val speciality: String
)

