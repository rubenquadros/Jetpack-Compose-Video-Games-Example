package com.ruben.epicworld.domain.repository

import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.domain.entity.games.GamesEntity
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.domain.entity.genres.GenresEntity

/**
 * Created by Ruben Quadros on 01/08/21
 **/
interface GamesRepository {

    suspend fun getAllGames(nextPage: Int): Record<GamesEntity>
    suspend fun getGameDetails(gameId: Int): Record<GameDetailsEntity>
    suspend fun getGameVideos(gameId: Int): Record<GameVideosEntity>
    suspend fun searchGames(query: String): Record<GamesEntity>
    suspend fun getGenres(): Record<GenresEntity>
}