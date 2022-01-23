package com.ruben.epicworld.presentation.home.ui

import androidx.paging.PagingData
import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.filters.FilterType
import com.ruben.epicworld.domain.entity.games.GameResultsEntity
import com.ruben.epicworld.presentation.base.ScreenState
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ruben Quadros on 01/08/21
 **/
data class HomeState(
    val screenState: ScreenState,
    val games: Flow<PagingData<GameResultsEntity>>?,
    val error: ErrorRecord?,
    val filters: Map<FilterType, List<Any>> = mutableMapOf(
        FilterType.SORT_ORDER to listOf(),
        FilterType.PLATFORMS to listOf(),
        FilterType.GENRES to listOf()
    )
)
