package com.rprikhodko.habrareader.home.news.domain

import com.rprikhodko.habrareader.common.data.dto.PostsPage
import retrofit2.Response

interface GetNewsUseCase {
    suspend operator fun invoke(page: Int): Response<PostsPage>
}