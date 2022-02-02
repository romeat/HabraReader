package com.rprihodko.habrareader.common.network

import javax.inject.Inject

class HabrRemoteData @Inject constructor(private val mainService : MainService) {

    suspend fun getNews(page: Int) = mainService.getPostsPage(null, null, null, null, true, page)

    suspend fun getBestArticlesOfPeriod(page: Int, period: Period) = mainService.getPostsPage(null, "date", null, period.stringValue, null, page)

    suspend fun getArticlesWithRating(page: Int, rating: Rating) = mainService.getPostsPage(null, "rating", rating.intValue, null, null, page)

    suspend fun getArticlesOfUser(page: Int, alias: String) = mainService.getPostsPageByCategory(user = alias, null, null, null, page)

    suspend fun getArticlesOfHub(page: Int, alias: String) = mainService.getPostsPageByCategory(null, null, hub = alias, "all", page)

    suspend fun getArticlesOfCompany(page: Int, alias: String) = mainService.getPostsPageByCategory(null, company = alias, null, null, page)

    suspend fun getAuthors(page: Int) = mainService.getUsersPage(page)

    suspend fun getAuthorProfile(alias: String) = mainService.getUserProfile(alias)

    suspend fun getHubs(page: Int) = mainService.getHubsPage(page)

    suspend fun getHubProfile(alias: String) = mainService.getHubProfile(alias)

    suspend fun getCompanies(page: Int) = mainService.getCompaniesPage(page, "rating", "desc")

    suspend fun getCompanyProfile(alias: String) = mainService.getCompanyProfile(alias)

    suspend fun getPost(id: Int) = mainService.getPost(id.toString())
}