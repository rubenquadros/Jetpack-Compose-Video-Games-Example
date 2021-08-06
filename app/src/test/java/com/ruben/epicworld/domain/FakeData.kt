package com.ruben.epicworld.domain

import com.ruben.epicworld.domain.entity.games.GameResultsEntity
import com.ruben.epicworld.domain.entity.games.GamesEntity

/**
 * Created by Ruben Quadros on 02/08/21
 **/
object FakeData {
    fun getFakeGames() = GamesEntity(getGamesEntity())

    private fun getGamesEntity(): List<GameResultsEntity> {
        val gameResults: ArrayList<GameResultsEntity> = ArrayList()
        gameResults.add(GameResultsEntity(
            1,
            "Max Payne",
            "",
            4.5,
            arrayListOf()
        ))
        gameResults.add(GameResultsEntity(
            2,
            "GTA V",
            "",
            4.8,
            arrayListOf()
        ))
        return gameResults
    }
}