package com.ruben.epicworld.repository.fakeimplementation.success

import com.ruben.epicworld.remote.model.request.GetAllGamesRequest
import com.ruben.epicworld.remote.model.request.GetGameDetailsRequest
import com.ruben.epicworld.remote.model.response.Filters
import com.ruben.epicworld.remote.model.response.GetAllGamesResponse
import com.ruben.epicworld.remote.model.response.common.AddedByStatus
import com.ruben.epicworld.remote.model.response.common.EsrbRating
import com.ruben.epicworld.remote.model.response.gamedetails.GetGameDetailsResponse
import com.ruben.epicworld.remote.model.response.gamedetails.Reactions
import com.ruben.epicworld.remote.rest.RestApi

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class FakeSuccessApi : RestApi {
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

    override suspend fun getGameDetails(getGameDetailsRequest: GetGameDetailsRequest): GetGameDetailsResponse {
        return GetGameDetailsResponse(
            123,
            "",
            "Max Payne",
            "Max Payne",
            "This is an action FPS game",
            1,
            arrayListOf(),
            "",
            false,
            "",
            "",
            "",
            "",
            4.5,
            5,
            arrayListOf(),
            Reactions(),
            2,
            AddedByStatus(),
            5,
            5,
            5,
            5,
            5,
            5,
            "",
            "",
            "",
            "",
            5,
            5,
            5,
            5,
            5,
            5,
            arrayListOf(),
            "",
            5,
            5,
            5,
            "",
            5,
            "",
            "",
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            EsrbRating(),
            "",
            ""
        )
    }
}