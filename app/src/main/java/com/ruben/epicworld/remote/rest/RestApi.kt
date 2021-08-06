package com.ruben.epicworld.remote.rest

import com.ruben.epicworld.remote.model.response.GetAllGamesResponse
import com.ruben.epicworld.remote.model.request.GetAllGamesRequest
import com.ruben.epicworld.remote.model.request.GetGameDetailsRequest
import com.ruben.epicworld.remote.model.response.gamedetails.GetGameDetailsResponse

/**
 * Created by Ruben Quadros on 01/08/21
 **/
interface RestApi {
    suspend fun getAllGames(getAllGamesRequest: GetAllGamesRequest): GetAllGamesResponse
    suspend fun getGameDetails(getGameDetailsRequest: GetGameDetailsRequest): GetGameDetailsResponse
}