package com.rprikhodko.habrareader.author.domain

import com.rprikhodko.habrareader.author.data.AuthorProfile
import com.rprikhodko.habrareader.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetAuthorProfileUseCaseImpl @Inject constructor(
    private val remoteData : HabrRemoteData
) : GetAuthorProfileUseCase {
    override suspend fun invoke(authorAlias: String): Response<AuthorProfile> = remoteData.getAuthorProfile(authorAlias)
}