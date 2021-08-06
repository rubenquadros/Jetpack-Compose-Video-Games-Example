package com.ruben.epicworld.data.mapper

import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.domain.entity.games.GamesEntity
import com.ruben.epicworld.remote.model.response.GetAllGamesResponse
import com.ruben.epicworld.remote.model.response.allgames.toEntity
import com.ruben.epicworld.remote.model.response.gamedetails.GetGameDetailsResponse
import com.ruben.epicworld.remote.model.response.gamedetails.toEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class AllGamesMapper {

    fun mapGamesResponse(allGamesResponse: GetAllGamesResponse): Record<GamesEntity> {
        return Record(GamesEntity(allGamesResponse.results.toEntity()), null)
    }

    fun mapGameDetailsResponse(gameDetailsResponse: GetGameDetailsResponse): Record<GameDetailsEntity> {
        return Record(gameDetailsResponse.toEntity(), null)
    }
}