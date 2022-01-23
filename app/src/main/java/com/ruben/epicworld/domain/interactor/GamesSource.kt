package com.ruben.epicworld.domain.interactor

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ruben.epicworld.domain.entity.filters.FilterType
import com.ruben.epicworld.domain.entity.filters.GamesFilterEntity
import com.ruben.epicworld.domain.entity.games.GameResultsEntity
import com.ruben.epicworld.domain.repository.GamesRepository
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class GamesSource @Inject constructor(private val gamesRepository: GamesRepository): PagingSource<Int, GameResultsEntity>() {

    private var ordering: String? = null
    private var platforms: String? = null
    private var genres: String? = null

    fun updateFilters(filters: List<GamesFilterEntity>) {
        fun getValue(list: List<Any>): String? {
            return if (list.isEmpty()) null
            else list.joinToString(",")
        }

        filters.forEach {
            when (it.type) {
                FilterType.SORT_ORDER -> {
                    ordering = getValue(it.selectedFilters)
                }
                FilterType.PLATFORMS -> {
                    platforms = getValue(it.selectedFilters)
                }
                FilterType.GENRES -> {
                    genres = getValue(it.selectedFilters)
                }
                else -> { /* do nothing */ }
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GameResultsEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GameResultsEntity> {
        val nextPage = params.key ?: 1
        val gamesResponse = gamesRepository.getAllGames(nextPage, ordering, platforms, genres)
        return if (gamesResponse.data == null) {
            LoadResult.Error(Exception(gamesResponse.error.toString()))
        } else {
            LoadResult.Page(
                data = gamesResponse.data.gameEntities,
                prevKey = if (nextPage == 1) null else nextPage-1,
                nextKey = nextPage.plus(1)
            )
        }
    }
}