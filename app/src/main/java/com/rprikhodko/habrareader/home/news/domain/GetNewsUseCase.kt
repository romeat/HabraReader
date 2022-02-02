package com.rprikhodko.habrareader.home.news.domain

import com.rprihodko.habrareader.common.dto.PostsPage
import retrofit2.Response

interface GetNewsUseCase {
    suspend operator fun invoke(page: Int): Response<PostsPage>
}