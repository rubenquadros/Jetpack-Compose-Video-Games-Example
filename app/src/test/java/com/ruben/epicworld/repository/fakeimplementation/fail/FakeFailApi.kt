package com.ruben.epicworld.repository.fakeimplementation.fail

import com.ruben.epicworld.remote.RemoteException
import com.ruben.epicworld.remote.model.request.GetAllGamesRequest
import com.ruben.epicworld.remote.model.request.GetGameDetailsRequest
import com.ruben.epicworld.remote.model.request.GetGameVideosRequest
import com.ruben.epicworld.remote.model.request.SearchGamesRequest
import com.ruben.epicworld.remote.model.response.allgames.GetAllGamesResponse
import com.ruben.epicworld.remote.model.response.gamedetails.GetGameDetailsResponse
import com.ruben.epicworld.remote.model.response.gamevideos.GetGameVideosResponse
import com.ruben.epicworld.remote.model.response.search.SearchGamesResponse
import com.ruben.epicworld.remote.rest.RestApi

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class FakeFailApi: RestApi {
    override suspend fun getAllGames(getAllGamesRequest: GetAllGamesRequest): GetAllGamesResponse {
        throw RemoteException.GenericError("Api Error")
    }

    override suspend fun getGameDetails(getGameDetailsRequest: GetGameDetailsRequest): GetGameDetailsResponse {
        throw RemoteException.ServerError("Server Error")
    }

    override suspend fun getGameVideos(getGameVideosRequest: GetGameVideosRequest): GetGameVideosResponse {
        throw RemoteException.NoNetworkError("No Network Error")
    }

    override suspend fun searchGames(searchGamesRequest: SearchGamesRequest): SearchGamesResponse {
        throw RemoteException.ClientError("Missing parameters")
    }
}