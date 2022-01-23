package com.ruben.epicworld.presentation.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.filters.FilterType
import com.ruben.epicworld.domain.entity.filters.GamesFilterEntity
import com.ruben.epicworld.domain.entity.games.GameResultsEntity
import com.ruben.epicworld.domain.interactor.GamesSource
import com.ruben.epicworld.presentation.base.BaseViewModel
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.home.ui.HomeSideEffect
import com.ruben.epicworld.presentation.home.ui.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 01/08/21
 **/
@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gamesSource: GamesSource
) : BaseViewModel<HomeState, HomeSideEffect>(savedStateHandle) {

    override fun createInitialState(): HomeState = HomeState(ScreenState.Loading, null, null)

    override fun initData() = intent {
        getAllGames(isInit = true)
    }

    fun handlePaginationDataError() = intent {
        reduce {
            Log.d("Ruben", "getAllGamesInternal reduce error")
            state.copy(screenState = ScreenState.Error,
                games = null,
                error = ErrorRecord.ServerError
            )
        }
    }

    fun handlePaginationAppendError(message: String, action: String) = intent {
        postSideEffect(HomeSideEffect.ShowSnackBar(message = message, action = action))
    }

    fun getAllGames(isInit: Boolean = false, isError: Boolean = false, filters: Map<FilterType, MutableList<Any>>? = null) = intent {
        Log.d("Ruben", "getAllGames -> isInit $isInit isError $isError filters $filters local map ${state.filters}")
        when {
            isInit -> {
                getAllGamesInternal()
            }
            isError -> {
                getAllGamesInternal(shouldUpdateFilters = true, filters = state.filters)
            }
            else -> {
                if (isFilterChanged(newFilters = filters, oldFilters = state.filters)) {
                    Log.d("Ruben", "getAllGames isFilterChanged true")
                    filters?.let {
                        Log.d("Ruben", "getAllGames before local map assign ${state.filters}")
                        getAllGamesInternal(shouldUpdateFilters = true, filters = filters)
                    }
                }
            }
        }
    }

    private fun isFilterChanged(newFilters: Map<FilterType, MutableList<Any>>?, oldFilters: Map<FilterType, List<Any>>): Boolean {
        Log.d("Ruben", "isFilterChanged -> filters $newFilters local map $oldFilters")
        newFilters?.forEach { filter ->
            when (filter.key) {
                FilterType.SORT_ORDER -> {
                    val sortFilters: List<Any>? = oldFilters[filter.key]
                    if (filter.value.size != sortFilters?.size) {
                        Log.d("Ruben", "isFilterChanged -> Sort size mismatch")
                        return true
                    }
                    sortFilters.forEach { sortFilter ->
                        if (filter.value.any { (it as? String) != (sortFilter as? String) }) {
                            Log.d("Ruben", "isFilterChanged -> Sort filter mismatch")
                            return true
                        }
                    }
                }
                FilterType.PLATFORMS -> {
                    val platformFilters: List<Any>? = oldFilters[filter.key]
                    if (filter.value.size != platformFilters?.size) {
                        Log.d("Ruben", "isFilterChanged -> Platform size mismatch")
                        return true
                    }
                    platformFilters.forEach { platformFilter ->
                        if (filter.value.any { (it as? Int) != (platformFilter as? Int) }) {
                            Log.d("Ruben", "isFilterChanged -> Platform filter mismatch")
                            return true
                        }
                    }
                }
                FilterType.GENRES -> {
                    val genreFilters: List<Any>? = oldFilters[filter.key]
                    if (filter.value.size != genreFilters?.size) {
                        Log.d("Ruben", "isFilterChanged -> Genre size mismatch")
                        return true
                    }
                    genreFilters.forEach { genreFilter ->
                        if (filter.value.any { (it as? Int) != (genreFilter as? Int) }) {
                            Log.d("Ruben", "isFilterChanged -> Genre filter mismatch")
                            return true
                        }
                    }
                }
                else -> {
                    Log.d("Ruben", "isFilterChanged -> Else part")
                    return true
                }
            }
        }
        return false
    }

    private fun getAllGamesInternal(shouldUpdateFilters: Boolean = false, filters: Map<FilterType, List<Any>>? = null) = intent {
        if (filters != null) {
            reduce {
                Log.d("Ruben", "getAllGamesInternal reduce filters")
                state.copy(filters = filters)
            }
        }
        if (shouldUpdateFilters) {
            val filtersList: MutableList<GamesFilterEntity> = mutableListOf()
            filters?.forEach { filter ->
                filtersList.add(GamesFilterEntity(type = filter.key, selectedFilters = filter.value))
            }
            gamesSource.updateFilters(filtersList.toList())
        }
        val result: Pager<Int, GameResultsEntity> = Pager(PagingConfig(50)) {
            gamesSource
        }
        reduce {
            Log.d("Ruben", "getAllGamesInternal reduce result")
            state.copy(screenState = ScreenState.Success, games = result.flow, error = null)
        }
    }
}