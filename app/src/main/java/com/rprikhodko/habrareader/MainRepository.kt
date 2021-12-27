package com.rprikhodko.habrareader

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteData : HabrRemoteData
) {

    suspend fun getMain(page : Int) = remoteData.getNewArticles(page)
}