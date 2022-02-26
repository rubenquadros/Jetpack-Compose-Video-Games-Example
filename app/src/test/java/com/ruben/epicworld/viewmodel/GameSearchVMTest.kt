package com.ruben.epicworld.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.games.GamesEntity
import com.ruben.epicworld.domain.interactor.GameSearchUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import com.ruben.epicworld.presentation.search.GameSearchViewModel
import com.ruben.epicworld.presentation.search.SearchSideEffect
import com.ruben.epicworld.presentation.search.SearchState
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.test

/**
 * Created by Ruben Quadros on 18/09/21
 **/
@ExperimentalCoroutinesApi
class GameSearchVMTest {

    private val mockRepository = mockk<GamesRepository>()
    private val useCase = mockk<GameSearchUseCase>()
    private val initialState = SearchState.InitialState
    private val savedStateHandle = SavedStateHandle()

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `vm should be able to manage view state`() = runTest(UnconfinedTestDispatcher()) {
        val gameSearchViewModel = GameSearchViewModel(
            savedStateHandle,
            useCase,
            UnconfinedTestDispatcher()
        ).test(initialState = initialState)
        coEvery { mockRepository.searchGames("gta") } answers {
            Record(GamesEntity(), null)
        }
        gameSearchViewModel.testIntent {
            searchGame("gta")
        }
        gameSearchViewModel.stateObserver.awaitCount(3)
        Assert.assertTrue(gameSearchViewModel.stateObserver.values.contains(SearchState.InitialState))
        Assert.assertTrue(gameSearchViewModel.sideEffectObserver.values.isEmpty())
    }

    @Test
    fun `vm should invoke use case to get search results`() = runTest(UnconfinedTestDispatcher()) {
        coEvery { mockRepository.searchGames("gta") } answers {
            Record(GamesEntity(), null)
        }
        coEvery { useCase.invoke(any(), any()) } answers { flow { emit(Record(GamesEntity(), null)) } }
        val gameSearchViewModel = GameSearchViewModel(
            savedStateHandle,
            useCase,
            UnconfinedTestDispatcher()
        )
        gameSearchViewModel.searchGame("abc")
        gameSearchViewModel.test(initialState = initialState).apply {
            sideEffectObserver.values.isEmpty()
            stateObserver.awaitCount(3)
            stateObserver.values.contains(SearchState.LoadingState)
            stateObserver.values.contains(SearchState.SearchResultState(arrayListOf()))
        }
    }

    @Test
    fun `vm should post side effect to navigate to details`() = runTest(UnconfinedTestDispatcher()) {
        val gameSearchViewModel = GameSearchViewModel(
            savedStateHandle,
            useCase,
            UnconfinedTestDispatcher()
        ).test(initialState = initialState)

        val success = gameSearchViewModel.testIntent {
            handleDetailsNavigation(1)
        }
        success.assert(initialState) {
            postedSideEffects(SearchSideEffect.NavigateToDetails(id = 1))
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }
}