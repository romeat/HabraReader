package com.rprikhodko.habrareader.network

import com.rprikhodko.habrareader.categories.authors.data.AuthorsPage
import com.rprikhodko.habrareader.categories.companies.data.CompaniesPage
import com.rprikhodko.habrareader.categories.hubs.data.HubsPage
import com.rprikhodko.habrareader.common.data.dto.PostsPage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MainService {
    @GET("articles/")
    suspend fun getPostsPage(@Query("hub") hub: String?,
                             @Query("sort") sort: String?,
                             @Query("score") score: Int?,
                             @Query("period") period: String?,
                             @Query("news") isNews: Boolean?,
                             @Query("page") page: Int
    ): Response<PostsPage>

    @GET("users/")
    suspend fun getUsersPage(@Query("page") page: Int): Response<AuthorsPage>

    @GET("hubs/")
    suspend fun getHubsPage(@Query("page") page: Int): Response<HubsPage>

    @GET("companies/")
    suspend fun getCompaniesPage(@Query("page") page: Int,
                                 @Query("order") order: String?,
                                 @Query("orderDirection") orderDirection: String?
    ): Response<CompaniesPage>

    //@GET("hubs/{hub}/profile/")
    //suspend fun getHubProfile(@Path("hub", encoded = true) hub: String): Response<HubDetails>

}