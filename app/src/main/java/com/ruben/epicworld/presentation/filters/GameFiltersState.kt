package com.ruben.epicworld.presentation.filters

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.ruben.epicworld.domain.entity.filters.PlatformFilterEntity
import com.ruben.epicworld.domain.entity.filters.SortFilterEntity
import com.ruben.epicworld.domain.entity.genres.GenresResultEntity

/**
 * Created by Ruben Quadros on 15/01/22
 **/
sealed class GameFiltersState(val isDataLoaded: Boolean) {
    var isFilterSelected: MutableState<Boolean> = mutableStateOf(false)
        private set
    object InitialState: GameFiltersState(false)
    object LoadingState: GameFiltersState(false)
    data class FilterState(
        val genres: List<GenresResultEntity>,
        val platforms: List<PlatformFilterEntity>,
        val sortOrders: List<SortFilterEntity>
    ) : GameFiltersState(true)
    object ErrorState: GameFiltersState(false)

    internal fun updateFilterSelected(isFilterSelected: Boolean) {
        this.isFilterSelected.value = isFilterSelected
    }
}
