package com.ruben.epicworld.domain.repository

import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.domain.entity.games.GamesEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
interface GamesRepository {

    suspend fun getAllGames(nextPage: Int): Record<GamesEntity>
    suspend fun getGameDetails(gameId: Int): Record<GameDetailsEntity>
}