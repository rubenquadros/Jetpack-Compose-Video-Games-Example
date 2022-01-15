package com.ruben.epicworld.presentation.filters

import com.ruben.epicworld.domain.entity.filters.PlatformFilterEntity
import com.ruben.epicworld.domain.entity.filters.SortFilterEntity
import com.ruben.epicworld.domain.entity.genres.GenresResultEntity

/**
 * Created by Ruben Quadros on 15/01/22
 **/
sealed class GameFiltersState {
    object InitialState: GameFiltersState()
    object LoadingState: GameFiltersState()
    data class FilterState(
        val genres: List<GenresResultEntity>,
        val platforms: List<PlatformFilterEntity>,
        val sortOrders: List<SortFilterEntity>
    ) : GameFiltersState()
    object ErrorState: GameFiltersState()
}
