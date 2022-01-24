package com.rprikhodko.habrareader.categories.authors.domain

import com.rprikhodko.habrareader.categories.authors.data.AuthorsPage
import com.rprikhodko.habrareader.network.HabrRemoteData
import retrofit2.Response
import javax.inject.Inject

class GetAuthorsUseCaseImpl @Inject constructor(
    private val remoteData : HabrRemoteData
) : GetAuthorsUseCase {
    override suspend fun invoke(page: Int): Response<AuthorsPage> = remoteData.getAuthors(page)
}