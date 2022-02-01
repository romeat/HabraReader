package com.rprikhodko.habrareader.post.domain

import com.rprikhodko.habrareader.post.data.PostPage
import retrofit2.Response

interface GetPostUseCase {
    suspend operator fun invoke(id: Int): Response<PostPage>
}