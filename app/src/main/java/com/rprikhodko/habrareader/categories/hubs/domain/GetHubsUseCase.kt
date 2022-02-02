package com.rprikhodko.habrareader.categories.hubs.domain

import com.rprihodko.habrareader.common.dto.HubsPage
import retrofit2.Response

interface GetHubsUseCase {
    suspend operator fun invoke(page: Int): Response<HubsPage>
}