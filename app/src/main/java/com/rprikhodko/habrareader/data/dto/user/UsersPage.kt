package com.rprikhodko.habrareader.data.dto.user

data class UsersPage(
    val pagesCount : Int,
    val authorRefs: Map<String, User>
)
