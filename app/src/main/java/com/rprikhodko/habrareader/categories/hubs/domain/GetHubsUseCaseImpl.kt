package com.rprikhodko.habrareader.categories.hubs.domain

import com.rprihodko.habrareader.common.dto.HubsPage
import com.rprihodko.habrareader.common.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetHubsUseCaseImpl @Inject constructor(
    private val remoteData : HabrRemoteData
) : GetHubsUseCase {
    override suspend fun invoke(page: Int): Response<HubsPage> = remoteData.getHubs(page)
}