package com.rprikhodko.habrareader.home.articles.data

import com.rprikhodko.habrareader.common.data.network.Period
import com.rprikhodko.habrareader.common.data.network.Rating
import com.rprikhodko.habrareader.network.HabrRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepository @Inject constructor(
    private val remoteData : HabrRemoteData
) {
    suspend fun getLatestArticles(page: Int, rating: Rating) = remoteData.getArticlesWithRating(page, rating)

    suspend fun getBestArticles(page: Int, period: Period) = remoteData.getBestArticlesOfPeriod(page, period)
}