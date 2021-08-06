package com.ruben.epicworld.domain.interactor

import com.ruben.epicworld.domain.BaseUseCase
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.domain.repository.GamesRepository
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 06/08/21
 **/
class GetGameDetailsUseCase @Inject constructor(private val gamesRepository: GamesRepository) :
    BaseUseCase<GetGameDetailsUseCase.RequestValue, Record<GameDetailsEntity>>() {

    override suspend fun run(request: RequestValue): Record<GameDetailsEntity> {
        return gamesRepository.getGameDetails(request.gameId)
    }

    data class RequestValue(
        val gameId: Int
    )
}