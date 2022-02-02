package com.rprihodko.habrareader.post.domain

import com.rprihodko.habrareader.common.dto.PostPage
import retrofit2.Response

interface GetPostUseCase {
    suspend operator fun invoke(id: Int): Response<PostPage>
}