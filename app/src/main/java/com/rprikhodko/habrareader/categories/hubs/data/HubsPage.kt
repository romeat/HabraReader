package com.rprikhodko.habrareader.categories.hubs.data

data class HubsPage(
    val pagesCount: Int,
    val hubIds: List<String>,
    val hubRefs: Map<String, HubPreview>
)
