package com.rprihodko.habrareader.common.dto

data class PostsPage(
    val pagesCount: Int,
    val articleIds: List<Int>,
    val articleRefs: Map<String, PostPreview>
)
