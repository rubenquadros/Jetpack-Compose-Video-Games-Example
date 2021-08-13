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

    fun getFakePagingData(): Flow<PagingData<GameResultsEntity>> {
        return flow {
            emit(
                PagingData.from(getFakeGames().gameEntities)
            )
        }
    }

    private fun getFakeGames() = GamesEntity(getGamesEntity())

    private fun getGamesEntity(): List<GameResultsEntity> {
        val gameResults: ArrayList<GameResultsEntity> = ArrayList()
        gameResults.add(
            GameResultsEntity(1, "Max Payne", "", 4.5)
        )
        gameResults.add(
            GameResultsEntity(2, "GTA V", "", 4.8)
        )
        gameResults.add(
            GameResultsEntity(3, "Portal 2", "", 4.7)
        )
        gameResults.add(
            GameResultsEntity(4, "Dota 2", "", 4.6)
        )
        gameResults.add(
            GameResultsEntity(5, "BioShock", "", 4.2)
        )
        gameResults.add(
            GameResultsEntity(6, "Limbo", "", 4.3)
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