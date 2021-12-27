package com.rprikhodko.habrareader.data.dto.hub

import com.rprikhodko.habrareader.data.dto.user.User

data class HubsPage(
    val pagesCount : Int,
    val hubRefs: Map<String, Hub>
)
