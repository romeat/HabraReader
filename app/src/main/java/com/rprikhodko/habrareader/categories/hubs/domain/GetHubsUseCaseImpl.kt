package com.rprikhodko.habrareader.categories.hubs.domain

import com.rprikhodko.habrareader.categories.hubs.data.HubsPage
import com.rprikhodko.habrareader.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetHubsUseCaseImpl @Inject constructor(
    private val remoteData : HabrRemoteData
) : GetHubsUseCase {
    override suspend fun invoke(page: Int): Response<HubsPage> = remoteData.getHubs(page)
}