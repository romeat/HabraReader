package com.rprikhodko.habrareader.author.domain

import com.rprihodko.habrareader.common.dto.AuthorProfile
import retrofit2.Response

interface GetAuthorProfileUseCase {
    suspend operator fun invoke(authorAlias: String): Response<AuthorProfile>
}