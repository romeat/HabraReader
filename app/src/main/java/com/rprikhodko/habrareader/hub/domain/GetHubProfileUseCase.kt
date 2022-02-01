package com.rprikhodko.habrareader.hub.domain

import com.rprikhodko.habrareader.hub.data.HubProfile
import retrofit2.Response

interface GetHubProfileUseCase {
    suspend operator fun invoke(hubAlias: String): Response<HubProfile>
}