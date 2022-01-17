package com.rprikhodko.habrareader.data.usecase

import com.rprikhodko.habrareader.PostsRepository
import com.rprikhodko.habrareader.data.dto.post.PostPreview
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
    private val repository: PostsRepository
) : GetPostsPageUseCase {
    override suspend fun invoke(page: Int): List<PostPreview> {
        val result = repository.getArticles(page)
        return if(result.isSuccessful) {
            val posts = result.body()?.articleRefs?.values?.toList()
            posts?.let {return it}
            return emptyList<PostPreview>()
        } else {
            emptyList<PostPreview>()
        }
    }
}