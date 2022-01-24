package com.rprikhodko.habrareader.network

import com.rprikhodko.habrareader.common.data.network.Period
import com.rprikhodko.habrareader.common.data.network.Rating
import javax.inject.Inject

class HabrRemoteData @Inject constructor(private val mainService : MainService) {

    suspend fun getNews(page: Int) = mainService.getPostsPage(null, null, null, null, true, page)

    suspend fun getBestArticlesOfPeriod(page: Int, period: Period) = mainService.getPostsPage(null, "date", null, period.stringValue, null, page)

    suspend fun getArticlesWithRating(page: Int, rating: Rating) = mainService.getPostsPage(null, "rating", rating.intValue, null, null, page)

    suspend fun getAuthors(page: Int) = mainService.getUsersPage(page)

    suspend fun getHubs(page: Int) = mainService.getHubsPage(page)

    suspend fun getCompanies(page: Int) = mainService.getCompaniesPage(page, "rating", "desc")
}