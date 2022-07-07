package com.ruben.epicworld.compose

import androidx.paging.PagingData
import com.ruben.epicworld.domain.entity.gamedetails.DevelopersEntity
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.domain.entity.gamedetails.GenresEntity
import com.ruben.epicworld.domain.entity.gamedetails.ParentPlatformsEntity
import com.ruben.epicworld.domain.entity.gamedetails.PlatformEntity
import com.ruben.epicworld.domain.entity.gamedetails.PlatformsEntity
import com.ruben.epicworld.domain.entity.gamedetails.PublishersEntity
import com.ruben.epicworld.domain.entity.gamedetails.RequirementsEntity
import com.ruben.epicworld.domain.entity.gamedetails.StoreEntity
import com.ruben.epicworld.domain.entity.gamedetails.StoresEntity
import com.ruben.epicworld.domain.entity.games.GameResultEntity
import com.ruben.epicworld.domain.entity.games.GameResultsEntity
import com.ruben.epicworld.domain.entity.games.GamesEntity
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.domain.entity.gamevideos.VideoResultEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by Ruben Quadros on 02/08/21
 **/
object FakeGamesData {

    fun getFakePagingData(): Flow<PagingData<GameResultEntity>> {
        return flow {
            emit(
                PagingData.from(getFakeGames().gameEntities)
            )
        }
    }

    private fun getFakeGames() = GamesEntity(getGamesEntity())

    private fun getGamesEntity(): List<GameResultEntity> {
        val gameResults: ArrayList<GameResultEntity> = ArrayList()
        gameResults.add(
            GameResultEntity(1, "Max Payne", "", 4.5)
        )
        gameResults.add(
            GameResultEntity(2, "GTA V", "", 4.8)
        )
        gameResults.add(
            GameResultEntity(3, "Portal 2", "", 4.7)
        )
        gameResults.add(
            GameResultEntity(4, "Dota 2", "", 4.6)
        )
        gameResults.add(
            GameResultEntity(5, "BioShock", "", 4.2)
        )
        gameResults.add(
            GameResultEntity(6, "Limbo", "", 4.3)
        )
        return gameResults
    }

    fun getFakeGameDetails() = GameDetailsEntity(123, "Max Payne",
        "Max Payne is a man with nothing to lose in the violent, cold urban night. A fugitive undercover cop framed for murder and now hunted by cops and the mob. Max is a man with his back against the wall, fighting a battle he cannot hope to win",
        4.5, "2013-09-17", "", 2, getParentPlatforms(), getPlatforms(), getStores(), getDevelopers(),
        getGenres(), getPublishers())

    fun getFakeGameDetailsShortDesc() = GameDetailsEntity(123, "Max Payne", "This is a cool shooting game!",
        4.5, "2013-09-17", "", 2, getParentPlatforms(), getPlatforms(), getStores(),
        getDevelopers(), getGenres(), getPublishers())

    fun getFakeGameDetailsNoVideos() = GameDetailsEntity(123, "Max Payne",
        "Max Payne is a man with nothing to lose in the violent, cold urban night. A fugitive undercover cop framed for murder and now hunted by cops and the mob. Max is a man with his back against the wall, fighting a battle he cannot hope to win",
        4.5, "2013-09-17", "", 0, getParentPlatforms(), getPlatforms(), getStores(),
        getDevelopers(), getGenres(), getPublishers())

    fun getFakeGameVideos() = GameVideosEntity(4, getVideos())

    fun getFakeSearchResults() = GameResultsEntity(getSearchResults())

    private fun getSearchResults(): List<GameResultEntity> {
        val results = arrayListOf<GameResultEntity>()
        results.add(
            GameResultEntity(1, "Grand Theft Auto", "", 4.5)
        )

        results.add(
            GameResultEntity(2, "GTA-bankrob", "", 4.3)
        )

        results.add(
            GameResultEntity(3, "GTA - Bombay", "", 4.6)
        )

        results.add(
            GameResultEntity(4, "GTA-V-DEMO", "", 4.1)
        )

        results.add(
            GameResultEntity(5, "gta multiplayer", "", 3.5)
        )

        results.add(
            GameResultEntity(6, "GTA Prototype", "", 4.0)
        )
        return results
    }

    private fun getVideos(): List<VideoResultEntity> =
        arrayListOf(
            VideoResultEntity(1, "", "GTA Online: Smuggler's Run Trailer", "https://steamcdn-a.akamaihd.net/steam/apps/256693661/movie_max.mp4"),
            VideoResultEntity(2, "", "GTA Online: Gunrunning Trailer", "https://steamcdn-a.akamaihd.net/steam/apps/256686767/movie_max.mp4"),
            VideoResultEntity(3, "", "GTA Online: Tiny Racers Trailer", "https://steamcdn-a.akamaihd.net/steam/apps/256683844/movie_max.mp4"),
            VideoResultEntity(4, "", "GTA Online Cunning Stunts: Special Vehicle Circuit Trailer", "https://steamcdn-a.akamaihd.net/steam/apps/256681415/movie_max.mp4")
        )

    private fun getParentPlatforms(): List<ParentPlatformsEntity> = arrayListOf()

    private fun getPlatforms(): List<PlatformsEntity> =
        arrayListOf(PlatformsEntity(
            PlatformEntity(4, "PlayStation 3", "", "", "", "", 10, ""),
            "", RequirementsEntity(), RequirementsEntity())
        )

    private fun getGenres(): List<GenresEntity> =
        arrayListOf(
            GenresEntity(123, "Action", "", 200, ""),
            GenresEntity(124, "Shooting", "", 250, "")
        )

    private fun getStores(): List<StoresEntity> =
        arrayListOf(
            StoresEntity(3, StoreEntity(3, "PlayStation Store", "", "", 10, ""))
        )

    private fun getDevelopers(): List<DevelopersEntity> =
        arrayListOf(
            DevelopersEntity(12, "Remedy Entertainment", "", 50, ""),
            DevelopersEntity(10, "Rockstar Games", "", 60, "")
        )

    private fun getPublishers(): List<PublishersEntity> =
        arrayListOf(
            PublishersEntity(1, "Rockstar Games", "", 20, ""),
            PublishersEntity(2, "Microsoft Studios", "", 10, "")
        )
}