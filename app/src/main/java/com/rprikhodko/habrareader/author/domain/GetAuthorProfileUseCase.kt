package com.rprikhodko.habrareader.author.domain

import com.rprikhodko.habrareader.author.data.AuthorProfile
import retrofit2.Response

interface GetAuthorProfileUseCase {
    suspend operator fun invoke(authorAlias: String): Response<AuthorProfile>
}