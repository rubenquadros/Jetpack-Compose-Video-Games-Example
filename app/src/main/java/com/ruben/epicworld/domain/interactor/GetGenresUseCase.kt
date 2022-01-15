package com.ruben.epicworld.domain.interactor

import com.ruben.epicworld.domain.BaseUseCase
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.genres.GenresEntity
import com.ruben.epicworld.domain.repository.GamesRepository
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 15/01/22
 **/
class GetGenresUseCase @Inject constructor(private val gamesRepository: GamesRepository) :
    BaseUseCase<Unit, Record<GenresEntity>>() {

    override suspend fun run(request: Unit): Record<GenresEntity> {
        return gamesRepository.getGenres()
    }
}