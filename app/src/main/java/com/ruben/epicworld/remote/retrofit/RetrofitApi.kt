package com.ruben.epicworld.remote.retrofit

import com.ruben.epicworld.remote.model.response.GetAllGamesResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Ruben Quadros on 01/08/21
 **/
interface RetrofitApi {

    @GET("/api/games")
    suspend fun getAllGames(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
    ): GetAllGamesResponse
}