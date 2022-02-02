package com.rprihodko.habrareader.common.dto

import com.rprihodko.habrareader.common.dto.UserScore

data class AuthorProfile(
    val alias: String,
    val avatarUrl: String?,
    val rating: Double,
    val speciality: String?,
    val scoreStats: UserScore,
    val registerDateTime: String,
    val counterStats: CounterStats,
)
