package com.rprikhodko.habrareader.author.data

import com.rprikhodko.habrareader.common.data.dto.UserScore

data class AuthorProfile(
    val alias: String,
    val avatarUrl: String?,
    val rating: Double,
    val speciality: String?,
    val scoreStats: UserScore,
    val registerDateTime: String,
    val counterStats: CounterStats,
)
