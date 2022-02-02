package com.rprihodko.habrareader.common.dto

data class AuthorsPage(
    val pagesCount: Int,
    val authorIds: List<String>,
    val authorRefs: Map<String, AuthorPreview>
)
