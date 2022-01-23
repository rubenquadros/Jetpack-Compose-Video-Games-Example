package com.ruben.epicworld.presentation.filters

import androidx.lifecycle.SavedStateHandle
import com.ruben.epicworld.domain.entity.filters.FilterType
import com.ruben.epicworld.domain.entity.filters.PlatformFilterEntity
import com.ruben.epicworld.domain.entity.filters.SortFilterEntity
import com.ruben.epicworld.domain.entity.genres.GenresResultEntity
import com.ruben.epicworld.domain.interactor.GetGenresUseCase
import com.ruben.epicworld.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 15/01/22
 **/
@HiltViewModel
class GameFiltersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGenresUseCase: GetGenresUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<GameFiltersState, GameFiltersSideEffect>(savedStateHandle) {

    private val filtersMap: MutableMap<FilterType, MutableList<Any>> = mutableMapOf(
        FilterType.SORT_ORDER to mutableListOf(),
        FilterType.PLATFORMS to mutableListOf(),
        FilterType.GENRES to mutableListOf()
    )
    private var genresList: List<GenresResultEntity> = emptyList()

    override fun createInitialState(): GameFiltersState {
        return GameFiltersState.InitialState
    }

    fun getGenres() = intent {
        if (state.isDataLoaded.not()) {
            reduce { GameFiltersState.LoadingState }.also {
                getGenresInternal()
            }
        } else {
            reduce { state }
        }
    }

    fun updateFilter(type: FilterType, value: Any, isAdded: Boolean) {
        if (isAdded) {
            filtersMap[type]?.add(value)
        } else {
            filtersMap[type]?.remove(value)
        }
        updateFilterInternal(type, value, isAdded)
    }

    fun clearFilters() = intent {
        fun clearMapValues() {
            filtersMap.values.forEach {
                it.clear()
            }
        }

        clearMapValues().also {
            reduce { GameFiltersState.LoadingState }
        }
        delay(100)
        getInitialFilterState()
    }

    fun getFiltersMap(): Map<FilterType, MutableList<Any>> {
        return filtersMap.toMap()
    }

    private fun getInitialFilterState() = intent {
        reduce {
            GameFiltersState.FilterState(
                sortOrders = getSortFilters(),
                platforms = getPlatformsFilter(),
                genres = genresList
            )
        }
    }

    private fun updateFilterInternal(type: FilterType, value: Any, isAdded: Boolean) = intent {
        (state as? GameFiltersState.FilterState)?.let { filterState ->
            when (type) {
                FilterType.SORT_ORDER -> {
                    val index =
                        filterState.sortOrders.indexOfFirst { it.value == (value as? String) }
                    if (index != -1) {
                        filterState.sortOrders[index].isSelected = isAdded
                    }
                }
                FilterType.PLATFORMS -> {
                    val index = filterState.platforms.indexOfFirst { it.id == (value as? Int) }
                    if (index != -1) {
                        filterState.platforms[index].isSelected = isAdded
                    }
                }
                FilterType.GENRES -> {
                    val index = filterState.genres.indexOfFirst { it.id == (value as? Int) }
                    if (index != -1) {
                        filterState.genres[index].isSelected = isAdded
                    }
                }
                else -> { /* do nothing */ }
            }
        }
        updateIsFilterSelectedInternal()
    }

    private fun updateIsFilterSelectedInternal() = intent {
        val isFilterSelected = (state as? GameFiltersState.FilterState)?.let { filterState ->
            filterState.sortOrders.any { it.isSelected } || filterState.platforms.any { it.isSelected } || filterState.genres.any { it.isSelected }
        }
        state.updateFilterSelected(isFilterSelected = isFilterSelected == true)
    }

    private fun getGenresInternal() = intent {
        getGenresUseCase.invoke(
            dispatcher,
            Unit
        ).collect { record ->
            reduce {
                if (record.data != null)  {
                    genresList = record.data.genres
                    GameFiltersState.FilterState(
                        genres = record.data.genres,
                        platforms = getPlatformsFilter(),
                        sortOrders = getSortFilters()
                    )
                } else {
                    GameFiltersState.ErrorState
                }
            }
        }
    }

    private fun getPlatformsFilter(): List<PlatformFilterEntity> {
        return listOf(
            PlatformFilterEntity(
                name = "PC",
                id = 1
            ),
            PlatformFilterEntity(
                name = "PlayStation",
                id = 2
            ),
            PlatformFilterEntity(
                name = "Xbox",
                id = 3
            )
        )
    }

    private fun getSortFilters(): List<SortFilterEntity> {
        return listOf(
            SortFilterEntity(
                name = "Name",
                value = "name"
            ),
            SortFilterEntity(
                name = "Released Date",
                value = "released"
            ),
            SortFilterEntity(
                name = "Rating",
                value = "rating"
            ),
            SortFilterEntity(
                name = "Critics Score",
                value = "metacritic"
            )
        )
    }
}