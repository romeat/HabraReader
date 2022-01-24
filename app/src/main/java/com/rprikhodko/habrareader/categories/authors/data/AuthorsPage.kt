package com.rprikhodko.habrareader.categories.authors.data

data class AuthorsPage(
    val pagesCount: Int,
    val authorIds: List<String>,
    val authorRefs: Map<String, AuthorPreview>
)
