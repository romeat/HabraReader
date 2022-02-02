package com.rprikhodko.habrareader.common.interfaces

import com.rprihodko.habrareader.common.dto.PostsPage
import retrofit2.Response

interface GetPostsByCategoryUseCase {
    suspend operator fun invoke(page: Int, alias: String): Response<PostsPage>
}