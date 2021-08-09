package com.ruben.epicworld.domain.interactor

import com.ruben.epicworld.domain.BaseUseCase
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.domain.repository.GamesRepository
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 09/08/21
 **/
class GetGameVideosUseCase @Inject constructor(private val gamesRepository: GamesRepository) :
    BaseUseCase<GetGameVideosUseCase.RequestValue, Record<GameVideosEntity>>() {

    override suspend fun run(request: RequestValue): Record<GameVideosEntity> {
        return gamesRepository.getGameVideos(request.gameId)
    }

    data class RequestValue(
        val gameId: Int
    )
}