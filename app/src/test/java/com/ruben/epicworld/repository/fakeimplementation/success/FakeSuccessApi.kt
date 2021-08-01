package com.ruben.epicworld.repository.fakeimplementation.success

import com.ruben.epicworld.remote.model.request.GetAllGamesRequest
import com.ruben.epicworld.remote.model.response.Filters
import com.ruben.epicworld.remote.model.response.GetAllGamesResponse
import com.ruben.epicworld.remote.rest.RestApi

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class FakeSuccessApi: RestApi {
    override suspend fun getAllGames(getAllGamesRequest: GetAllGamesRequest): GetAllGamesResponse {
        return GetAllGamesResponse(
            590937,
            "https://api.rawg.io/api/games?key=2a8418325c0647209461250576288706&page=2&page_size=50",
            null,
            arrayListOf(),
            "All Games",
            "",
            "",
            "All Games",
            false,
            false,
            "",
            Filters(
                arrayListOf()
            ),
            arrayListOf()
        )
    }
}