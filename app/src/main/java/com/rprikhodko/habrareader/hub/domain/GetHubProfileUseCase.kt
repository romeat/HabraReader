package com.rprikhodko.habrareader.hub.domain

import com.rprihodko.habrareader.common.dto.HubProfile
import retrofit2.Response

interface GetHubProfileUseCase {
    suspend operator fun invoke(hubAlias: String): Response<HubProfile>
}