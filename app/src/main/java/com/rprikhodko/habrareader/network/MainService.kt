package com.rprikhodko.habrareader.network

import com.rprikhodko.habrareader.author.data.AuthorProfile
import com.rprikhodko.habrareader.categories.authors.data.AuthorsPage
import com.rprikhodko.habrareader.categories.companies.data.CompaniesPage
import com.rprikhodko.habrareader.categories.hubs.data.HubsPage
import com.rprikhodko.habrareader.common.data.dto.PostsPage
import com.rprikhodko.habrareader.company.data.CompanyProfile
import com.rprikhodko.habrareader.hub.data.HubProfile
import com.rprikhodko.habrareader.post.data.PostPage
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

    @GET("articles/")
    suspend fun getPostsPageByCategory(@Query("user") user: String?,
                                @Query("company") company: String?,
                                @Query("hub") hub: String?,
                                @Query("sort") sort: String?,
                                @Query("page") page: Int
    ): Response<PostsPage>

    @GET("articles/{article}/")
    suspend fun getPost(@Path("article", encoded = true) article: String): Response<PostPage>

    @GET("users/")
    suspend fun getUsersPage(@Query("page") page: Int): Response<AuthorsPage>

    @GET("users/{user_alias}/card/")
    suspend fun getUserProfile(@Path("user_alias", encoded = true) userAlias: String): Response<AuthorProfile>

    @GET("hubs/")
    suspend fun getHubsPage(@Query("page") page: Int): Response<HubsPage>

    @GET("hubs/{hub_alias}/profile/")
    suspend fun getHubProfile(@Path("hub_alias", encoded = true) hubAlias: String): Response<HubProfile>

    @GET("companies/")
    suspend fun getCompaniesPage(@Query("page") page: Int,
                                 @Query("order") order: String?,
                                 @Query("orderDirection") orderDirection: String?
    ): Response<CompaniesPage>

    @GET("companies/{company_alias}/card/")
    suspend fun getCompanyProfile(@Path("company_alias", encoded = true) companyAlias: String): Response<CompanyProfile>

}