package com.rprikhodko.habrareader

import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService : MainService) {

    suspend fun getMain(page : Int) = mainService.getMain("rating", "ru", "ru", page)
}