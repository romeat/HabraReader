package com.rprikhodko.habrareader.categories.authors.domain

import com.rprikhodko.habrareader.categories.authors.data.AuthorsPage
import retrofit2.Response

interface GetAuthorsUseCase {
    suspend operator fun invoke(page: Int): Response<AuthorsPage>
}