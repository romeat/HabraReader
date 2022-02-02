package com.rprihodko.habrareader.common.dto

data class HubsPage(
    val pagesCount: Int,
    val hubIds: List<String>,
    val hubRefs: Map<String, HubPreview>
)
