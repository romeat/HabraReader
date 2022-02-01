package com.rprikhodko.habrareader.author.domain

import com.rprikhodko.habrareader.common.data.dto.PostsPage
import com.rprikhodko.habrareader.common.interfaces.GetPostsByCategoryUseCase
import com.rprikhodko.habrareader.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetAuthorPostsUseCase @Inject constructor(
    private val remoteData : HabrRemoteData,
) : GetPostsByCategoryUseCase {
    override suspend fun invoke(page: Int, alias: String): Response<PostsPage> = remoteData.getArticlesOfUser(page, alias)
}