package com.rprikhodko.habrareader.categories.authors.domain

import com.rprihodko.habrareader.common.dto.AuthorsPage
import retrofit2.Response

interface GetAuthorsUseCase {
    suspend operator fun invoke(page: Int): Response<AuthorsPage>
}