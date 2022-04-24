package com.ruben.epicworld.domain

import com.ruben.epicworld.domain.entity.games.GameResultEntity
import com.ruben.epicworld.domain.entity.games.GamesEntity

/**
 * Created by Ruben Quadros on 02/08/21
 **/
object FakeData {
    fun getFakeGames() = GamesEntity(getGamesEntity())

    private fun getGamesEntity(): List<GameResultEntity> {
        val gameResults: ArrayList<GameResultEntity> = ArrayList()
        gameResults.add(GameResultEntity(
            1,
            "Max Payne",
            "",
            4.5,
        ))
        gameResults.add(GameResultEntity(
            2,
            "GTA V",
            "",
            4.8,
        ))
        return gameResults
    }
}