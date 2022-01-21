package com.rprikhodko.habrareader.network

import com.rprikhodko.habrareader.data.network.Period
import com.rprikhodko.habrareader.data.network.Rating
import javax.inject.Inject

class HabrRemoteData @Inject constructor(private val mainService : MainService) {

    suspend fun getNewArticles(page: Int) = mainService.getPostsPage(null, "rating", null, null, null, page)

    suspend fun getNews(page: Int) = mainService.getPostsPage(null, null, null, null, true, page)

    suspend fun getBestArticlesOfPeriod(page: Int, period: Period) = mainService.getPostsPage(null, "date", null, period.stringValue, null, page)

    suspend fun getArticlesWithRating(page: Int, rating: Rating) = mainService.getPostsPage(null, "rating", rating.intValue, null, null, page)

}