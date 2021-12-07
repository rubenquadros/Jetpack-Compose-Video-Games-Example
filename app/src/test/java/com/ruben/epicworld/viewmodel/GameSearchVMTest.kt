package com.ruben.epicworld.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.ruben.epicworld.CoroutinesTestRule
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.games.GamesEntity
import com.ruben.epicworld.domain.interactor.GameSearchUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import com.ruben.epicworld.presentation.search.GameSearchViewModel
import com.ruben.epicworld.presentation.search.SearchState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.orbitmvi.orbit.test

/**
 * Created by Ruben Quadros on 18/09/21
 **/
@ExperimentalCoroutinesApi
class GameSearchVMTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val mockRepository = mockk<GamesRepository>()
    private val useCase = mockk<GameSearchUseCase>()
    private val initialState = SearchState.InitialState
    private val savedStateHandle = SavedStateHandle()

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `vm should be able to manager view state`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val gameSearchViewModel = GameSearchViewModel(
            savedStateHandle,
            useCase,
            coroutinesTestRule.testDispatcher
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
    fun `vm should invoke use case to get search results`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        coEvery { mockRepository.searchGames("gta") } answers {
            Record(GamesEntity(), null)
        }
        coEvery { useCase.invoke(any(), any(), any(), any()) } answers { Record(GamesEntity(), null) }
        val gameSearchViewModel = GameSearchViewModel(
            savedStateHandle,
            useCase,
            coroutinesTestRule.testDispatcher
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
    fun `vm should post side effect to navigate to details`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val gameSearchViewModel = GameSearchViewModel(
            savedStateHandle,
            useCase,
            coroutinesTestRule.testDispatcher
        ).test(initialState = initialState)

        gameSearchViewModel.testIntent {
            handleDetailsNavigation(1)
        }
        gameSearchViewModel.sideEffectObserver.awaitCount(1)
        Assert.assertTrue(gameSearchViewModel.sideEffectObserver.values.isNotEmpty())
    }
}