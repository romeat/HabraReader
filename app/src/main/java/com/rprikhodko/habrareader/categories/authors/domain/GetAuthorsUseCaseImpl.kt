package com.rprikhodko.habrareader.categories.authors.domain

import com.rprihodko.habrareader.common.dto.AuthorsPage
import com.rprihodko.habrareader.common.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetAuthorsUseCaseImpl @Inject constructor(
    private val remoteData : HabrRemoteData
) : GetAuthorsUseCase {
    override suspend fun invoke(page: Int): Response<AuthorsPage> = remoteData.getAuthors(page)
}