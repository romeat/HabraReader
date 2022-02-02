package com.rprihodko.habrareader.common.dto

data class PostStatistics(
    val commentsCount: Int,
    val readingCount: Int,
    val favoritesCount: Int,
    val score: Int,
    val votesCount: Int
)
