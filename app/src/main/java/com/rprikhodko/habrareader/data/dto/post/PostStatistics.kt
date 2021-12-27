package com.rprikhodko.habrareader.data.dto.post

data class PostStatistics(
    val commentsCount: Int,
    val readingCount: Int,
    val favoritesCount: Int,
    val score: Int,
    val votesCount: Int
)
