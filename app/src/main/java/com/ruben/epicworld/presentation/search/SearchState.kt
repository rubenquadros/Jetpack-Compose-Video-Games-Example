package com.ruben.epicworld.presentation.search

import com.ruben.epicworld.domain.entity.games.GameResultsEntity

/**
 * Created by Ruben Quadros on 18/09/21
 **/
sealed class SearchState {
    object InitialState: SearchState()
    object LoadingState: SearchState()
    data class SearchResultState(val searchResults: GameResultsEntity): SearchState()
    object NoResultsState: SearchState()
    object ErrorState: SearchState()
}