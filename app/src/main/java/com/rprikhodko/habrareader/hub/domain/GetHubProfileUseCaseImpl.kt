package com.rprikhodko.habrareader.hub.domain

import com.rprihodko.habrareader.common.dto.HubProfile
import com.rprihodko.habrareader.common.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetHubProfileUseCaseImpl @Inject constructor(
    private val remoteData : HabrRemoteData
) : GetHubProfileUseCase {
    override suspend fun invoke(hubAlias: String): Response<HubProfile> = remoteData.getHubProfile(hubAlias)
}