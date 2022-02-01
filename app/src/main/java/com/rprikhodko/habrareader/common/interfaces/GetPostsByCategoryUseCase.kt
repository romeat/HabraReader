package com.rprikhodko.habrareader.common.interfaces

import com.rprikhodko.habrareader.common.data.dto.PostsPage
import retrofit2.Response

interface GetPostsByCategoryUseCase {
    suspend operator fun invoke(page: Int, alias: String): Response<PostsPage>
}