package com.ruben.epicworld.remote.rest

import com.ruben.epicworld.remote.model.request.GetAllGamesRequest
import com.ruben.epicworld.remote.model.request.GetGameDetailsRequest
import com.ruben.epicworld.remote.model.request.GetGameVideosRequest
import com.ruben.epicworld.remote.model.request.SearchGamesRequest
import com.ruben.epicworld.remote.model.response.allgames.GetAllGamesResponse
import com.ruben.epicworld.remote.model.response.gamedetails.GetGameDetailsResponse
import com.ruben.epicworld.remote.model.response.gamevideos.GetGameVideosResponse
import com.ruben.epicworld.remote.model.response.genres.GetGenresResponse
import com.ruben.epicworld.remote.model.response.search.SearchGamesResponse
import com.ruben.epicworld.remote.retrofit.RetrofitApi
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class RestApiImpl @Inject constructor(private val retrofitApi: RetrofitApi): RestApi {

    override suspend fun getAllGames(getAllGamesRequest: GetAllGamesRequest): GetAllGamesResponse {
        return retrofitApi.getAllGames(
            page = getAllGamesRequest.nextPage,
            pageSize = getAllGamesRequest.pageSize,
            ordering = getAllGamesRequest.ordering,
            platforms = getAllGamesRequest.platforms,
            genres = getAllGamesRequest.genres
        )
    }

    override suspend fun getGameDetails(getGameDetailsRequest: GetGameDetailsRequest): GetGameDetailsResponse {
        return retrofitApi.getGameDetails(getGameDetailsRequest.gameId)
    }

    override suspend fun getGameVideos(getGameVideosRequest: GetGameVideosRequest): GetGameVideosResponse {
        return retrofitApi.getGameVideos(getGameVideosRequest.gameId)
    }

    override suspend fun searchGames(searchGamesRequest: SearchGamesRequest): SearchGamesResponse {
        return retrofitApi.searchGames(searchGamesRequest.query)
    }

    override suspend fun getGenres(): GetGenresResponse {
        return retrofitApi.getGenres()
    }

}