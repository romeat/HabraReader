package com.rprikhodko.habrareader.author.domain

import com.rprihodko.habrareader.common.dto.AuthorProfile
import com.rprihodko.habrareader.common.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetAuthorProfileUseCaseImpl @Inject constructor(
    private val remoteData : HabrRemoteData
) : GetAuthorProfileUseCase {
    override suspend fun invoke(authorAlias: String): Response<AuthorProfile> = remoteData.getAuthorProfile(authorAlias)
}