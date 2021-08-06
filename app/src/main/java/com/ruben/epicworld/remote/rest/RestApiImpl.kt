package com.ruben.epicworld.remote.rest

import com.ruben.epicworld.remote.model.response.GetAllGamesResponse
import com.ruben.epicworld.remote.model.request.GetAllGamesRequest
import com.ruben.epicworld.remote.retrofit.RetrofitApi
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class RestApiImpl @Inject constructor(private val retrofitApi: RetrofitApi): RestApi {

    override suspend fun getAllGames(getAllGamesRequest: GetAllGamesRequest): GetAllGamesResponse {
        return retrofitApi.getAllGames(getAllGamesRequest.nextPage, getAllGamesRequest.pageSize)
    }

}