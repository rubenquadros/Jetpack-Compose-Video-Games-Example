package com.ruben.epicworld.data.repository

import com.ruben.epicworld.data.DataSource
import com.ruben.epicworld.data.mapper.AllGamesMapper
import com.ruben.epicworld.data.mapper.ErrorMapper
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.domain.entity.games.GamesEntity
import com.ruben.epicworld.domain.repository.GamesRepository
import com.ruben.epicworld.remote.RemoteException
import com.ruben.epicworld.remote.model.request.GetAllGamesRequest
import com.ruben.epicworld.remote.model.request.GetGameDetailsRequest
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class GamesRepositoryImpl @Inject constructor(private val dataSource: DataSource): GamesRepository {

    private val gamesMapper = AllGamesMapper()
    private val errorMapper = ErrorMapper()

    override suspend fun getAllGames(nextPage: Int): Record<GamesEntity> {
        return try {
            dataSource.api().restApi().getAllGames(GetAllGamesRequest(nextPage)).run {
                gamesMapper.mapGamesResponse(this)
            }
        } catch (e: RemoteException) {
            errorMapper.mapErrorRecord(e)
        }
    }

    override suspend fun getGameDetails(gameId: Int): Record<GameDetailsEntity> {
        return try {
            dataSource.api().restApi().getGameDetails(GetGameDetailsRequest(gameId)).run {
                gamesMapper.mapGameDetailsResponse(this)
            }
        } catch (e: RemoteException) {
            errorMapper.mapErrorRecord(e)
        }
    }
}