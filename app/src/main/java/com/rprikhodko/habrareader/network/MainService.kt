package com.rprikhodko.habrareader.network

import com.rprikhodko.habrareader.data.dto.hub.HubDetails
import com.rprikhodko.habrareader.common.data.dto.PostsPage
import com.rprikhodko.habrareader.data.dto.hub.HubsPage
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
    suspend fun getUsersPage(@Query("page") page: Int): Response<PostsPage>

    @GET("hubs/")
    suspend fun getHubsPage(@Query("page") page: Int): Response<HubsPage>

    @GET("hubs/{hub}/profile/")
    suspend fun getHubProfile(@Path("hub", encoded = true) hub: String): Response<HubDetails>


}