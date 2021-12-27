package com.rprikhodko.habrareader.data.dto.post

import com.rprikhodko.habrareader.data.dto.post.PostPreview

data class PostsPage(
    val pagesCount: Int,
    val articleIds: List<Int>,
    val articleRefs: Map<String, PostPreview>
)
