package com.rprihodko.habrareader.post.domain


import com.rprihodko.habrareader.common.dto.PostPage
import com.rprihodko.habrareader.common.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetPostUseCaseImpl @Inject constructor(
    private val remoteData : HabrRemoteData
) : GetPostUseCase{
    override suspend fun invoke(id: Int): Response<PostPage> = remoteData.getPost(id)
}