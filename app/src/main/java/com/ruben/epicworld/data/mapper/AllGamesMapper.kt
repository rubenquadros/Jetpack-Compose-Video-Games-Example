package com.ruben.epicworld.data.mapper

import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.games.GamesEntity
import com.ruben.epicworld.remote.model.response.GetAllGamesResponse
import com.ruben.epicworld.remote.model.response.toEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class AllGamesMapper {

    fun mapGamesResponse(allGamesResponse: GetAllGamesResponse): Record<GamesEntity> {
        return Record(GamesEntity(allGamesResponse.results.toEntity()), null)
    }
}