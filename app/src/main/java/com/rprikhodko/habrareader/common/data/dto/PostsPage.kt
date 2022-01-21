package com.rprikhodko.habrareader.common.data.dto

data class PostsPage(
    val pagesCount: Int,
    val articleIds: List<Int>,
    val articleRefs: Map<String, PostPreview>
)
