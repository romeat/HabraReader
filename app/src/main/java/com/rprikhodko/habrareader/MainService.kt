package com.rprikhodko.habrareader

import com.rprikhodko.habrareader.data.MainPage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MainService {
    @GET("articles/")
    suspend fun getMain(@Query("sort") sortt: String,
                        @Query("fl") fl: String,
                        @Query("hl") hl: String,
                        @Query("page") page: Int
    ): Response<MainPage>
}