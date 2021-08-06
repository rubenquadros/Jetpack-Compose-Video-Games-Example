package com.ruben.epicworld.domain

import com.ruben.epicworld.domain.entity.games.AddedByStatusEntity
import com.ruben.epicworld.domain.entity.games.EsrbRatingEntity
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
            "",
            "Max Payne",
            "",
            false,
            "",
            4.5,
            5,
            100,
            arrayListOf(),
            123,
            12,
            AddedByStatusEntity(1,2,3,4,5,6),
            10,
            90,
            98,
            "",
            "",
            13,
            "",
            "",
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            "",
            arrayListOf(),
            EsrbRatingEntity(1, "", ""),
            arrayListOf()
        ))
        gameResults.add(GameResultsEntity(
            2,
            "",
            "GTA V",
            "",
            false,
            "",
            4.8,
            5,
            120,
            arrayListOf(),
            1234,
            12,
            AddedByStatusEntity(1,2,3,4,5,6),
            10,
            90,
            98,
            "",
            "",
            13,
            "",
            "",
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            "",
            arrayListOf(),
            EsrbRatingEntity(1, "", ""),
            arrayListOf()
        ))
        return gameResults
    }
}