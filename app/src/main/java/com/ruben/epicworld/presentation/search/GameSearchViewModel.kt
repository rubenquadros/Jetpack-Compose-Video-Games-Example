package com.ruben.epicworld.presentation.search

import androidx.lifecycle.SavedStateHandle
import com.ruben.epicworld.domain.entity.games.GameResultsEntity
import com.ruben.epicworld.domain.interactor.GameSearchUseCase
import com.ruben.epicworld.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 12/09/21
 **/
@HiltViewModel
class GameSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gameSearchUseCase: GameSearchUseCase
): BaseViewModel<SearchState, SearchSideEffect>(savedStateHandle) {

    private var searchQuery = ""
    private val searchFlow: MutableSharedFlow<String> = MutableSharedFlow()

    override fun createInitialState(): SearchState = SearchState.InitialState

    override fun initData() {
        handleSearch()
    }

    fun searchGame(query: String? = null) = intent {
        query?.let { this@GameSearchViewModel.searchQuery =  it }
        searchFlow.emit(query ?: searchQuery)
    }

    fun handleDetailsNavigation(gameId: Int) = intent {
        postSideEffect(SearchSideEffect.NavigateToDetails(gameId))
    }

    @OptIn(FlowPreview::class)
    private fun handleSearch() = intent {
        searchFlow.debounce {
            if (it.length > 3) 500 else 0
        }.collect {
            reduce {
                SearchState.LoadingState
            }
            gameSearchUseCase.invoke(
                GameSearchUseCase.RequestValue(it)
            ).collect { record ->
                reduce {
                    if (record.data != null) {
                        if (record.data.gameEntities.isEmpty()) {
                            SearchState.NoResultsState
                        } else {
                            SearchState.SearchResultState(
                                searchResults = GameResultsEntity(
                                    gameResults = record.data.gameEntities
                                )
                            )
                        }
                    } else {
                        SearchState.ErrorState
                    }
                }
            }
        }
    }

}