package com.ruben.epicworld.presentation.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ruben.epicworld.domain.entity.filters.PlatformFilterEntity
import com.ruben.epicworld.domain.entity.filters.SortFilterEntity
import com.ruben.epicworld.domain.interactor.GetGenresUseCase
import com.ruben.epicworld.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    override fun createInitialState(): GameFiltersState = GameFiltersState.InitialState

    override fun initData() {
        super.initData()
        intent { reduce { GameFiltersState.LoadingState }.also {
            getGenresInternal()
        } }
    }

    private fun getGenresInternal() = intent {
        getGenresUseCase.invoke(
            viewModelScope,
            dispatcher,
            Unit
        ) { record ->
            viewModelScope.launch {
                reduce {
                    if (record?.data != null)  {
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