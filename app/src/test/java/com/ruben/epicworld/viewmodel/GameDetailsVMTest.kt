package com.ruben.epicworld.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.ruben.epicworld.CoroutinesTestRule
import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.domain.interactor.GetGameDetailsUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.details.GameDetailsState
import com.ruben.epicworld.presentation.details.GameDetailsViewModel
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
 * Created by Ruben Quadros on 07/08/21
 **/
@ExperimentalCoroutinesApi
class GameDetailsVMTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val mockRepository = mockk<GamesRepository>()
    private val useCase = GetGameDetailsUseCase(mockRepository)
    private val initialState = GameDetailsState(ScreenState.Loading, null, null)
    private val savedStateHandle = SavedStateHandle().apply {
        set("gameId", 2)
    }

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `vm should invoke use case to get game details`() = coroutinesTestRule.testCoroutineScope.runBlockingTest {
        val gameDetailsViewModel = GameDetailsViewModel(
            savedStateHandle,
            useCase,
            coroutinesTestRule.testDispatcher
        ).test(initialState = initialState)
        coEvery { mockRepository.getGameDetails(2) } answers {
            Record(GameDetailsEntity(), null)
        }
        gameDetailsViewModel.testIntent {
            getGameDetails(2)
        }
        gameDetailsViewModel.stateObserver.awaitCount(2)
        Assert.assertTrue(gameDetailsViewModel.stateObserver.values.last().screenState == ScreenState.Success)
        Assert.assertTrue(gameDetailsViewModel.stateObserver.values.last().gameDetails != null)
        Assert.assertTrue(gameDetailsViewModel.stateObserver.values.last().error == null)
        Assert.assertTrue(gameDetailsViewModel.sideEffectObserver.values.isEmpty())
    }

    @Test
    fun `vm should post side effect when there is error in getting game details`() = coroutinesTestRule.testCoroutineScope.runBlockingTest {
        val gameDetailsViewModel = GameDetailsViewModel(
            savedStateHandle,
            useCase,
            coroutinesTestRule.testDispatcher
        ).test(initialState = initialState)
        coEvery { mockRepository.getGameDetails(2) } answers {
            Record(null, ErrorRecord.GenericError)
        }
        gameDetailsViewModel.testIntent {
            getGameDetails(2)
        }
        gameDetailsViewModel.testIntent {
            handleGameIdError()
        }
        gameDetailsViewModel.sideEffectObserver.awaitCount(1)
        Assert.assertTrue(gameDetailsViewModel.sideEffectObserver.values.isNotEmpty())
        gameDetailsViewModel.stateObserver.awaitCount(2)
        Assert.assertTrue(gameDetailsViewModel.stateObserver.values.last().screenState == ScreenState.Error)
        Assert.assertTrue(gameDetailsViewModel.stateObserver.values.last().gameDetails == null)
        Assert.assertTrue(gameDetailsViewModel.stateObserver.values.last().error != null)
    }

    @Test
    fun `vm should post side effect when there is error with game id`() = coroutinesTestRule.testCoroutineScope.runBlockingTest {
        val gameDetailsViewModel = GameDetailsViewModel(
            savedStateHandle,
            useCase,
            coroutinesTestRule.testDispatcher
        ).test(initialState = initialState)
        gameDetailsViewModel.testIntent {
            handleGameIdError()
        }
        gameDetailsViewModel.sideEffectObserver.awaitCount(1)
        Assert.assertTrue(gameDetailsViewModel.sideEffectObserver.values.isNotEmpty())
    }

}