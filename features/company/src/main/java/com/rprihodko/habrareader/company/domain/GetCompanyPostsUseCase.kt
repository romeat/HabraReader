package com.rprihodko.habrareader.company.domain

import com.rprihodko.habrareader.common.dto.PostsPage
import com.rprihodko.habrareader.common.interfaces.GetPostsByCategoryUseCase
import com.rprihodko.habrareader.common.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetCompanyPostsUseCase @Inject constructor(
    private val remoteData : HabrRemoteData
) : GetPostsByCategoryUseCase {
    override suspend fun invoke(page: Int, alias: String): Response<PostsPage> = remoteData.getArticlesOfCompany(page, alias)
}