package com.ruben.epicworld.remote.rest

import com.ruben.epicworld.remote.model.request.GetAllGamesRequest
import com.ruben.epicworld.remote.model.request.GetGameDetailsRequest
import com.ruben.epicworld.remote.model.request.GetGameVideosRequest
import com.ruben.epicworld.remote.model.response.allgames.GetAllGamesResponse
import com.ruben.epicworld.remote.model.response.gamedetails.GetGameDetailsResponse
import com.ruben.epicworld.remote.model.response.gamevideos.GetGameVideosResponse

/**
 * Created by Ruben Quadros on 01/08/21
 **/
interface RestApi {
    suspend fun getAllGames(getAllGamesRequest: GetAllGamesRequest): GetAllGamesResponse
    suspend fun getGameDetails(getGameDetailsRequest: GetGameDetailsRequest): GetGameDetailsResponse
    suspend fun getGameVideos(getGameVideosRequest: GetGameVideosRequest): GetGameVideosResponse
}