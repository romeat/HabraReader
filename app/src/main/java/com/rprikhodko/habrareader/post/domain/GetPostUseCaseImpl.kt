package com.rprikhodko.habrareader.post.domain

import com.rprikhodko.habrareader.network.HabrRemoteData
import com.rprikhodko.habrareader.post.data.PostPage
import retrofit2.Response
import javax.inject.Inject

class GetPostUseCaseImpl @Inject constructor(
    private val remoteData : HabrRemoteData
) : GetPostUseCase{
    override suspend fun invoke(id: Int): Response<PostPage> = remoteData.getPost(id)
}