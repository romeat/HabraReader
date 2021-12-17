package com.rprikhodko.habrareader

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteData : MainRemoteData
) {

    suspend fun getMain(page : Int) = remoteData.getMain(page)
}