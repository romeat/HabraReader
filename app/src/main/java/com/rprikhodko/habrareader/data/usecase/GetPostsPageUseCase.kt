package com.rprikhodko.habrareader.data.usecase

import com.rprikhodko.habrareader.data.dto.post.PostPreview

interface GetPostsPageUseCase {
    suspend operator fun invoke(page: Int): List<PostPreview>
}