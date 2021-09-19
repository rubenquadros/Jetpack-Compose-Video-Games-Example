package com.ruben.epicworld.domain.interactor

import com.ruben.epicworld.domain.BaseUseCase
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.games.GamesEntity
import com.ruben.epicworld.domain.repository.GamesRepository
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 18/09/21
 **/
class GameSearchUseCase @Inject constructor(private val gamesRepository: GamesRepository) :
    BaseUseCase<GameSearchUseCase.RequestValue, Record<GamesEntity>>() {

    override suspend fun run(request: RequestValue): Record<GamesEntity> {
        return gamesRepository.searchGames(request.searchQuery)
    }

    data class RequestValue(val searchQuery: String)
}