package com.ruben.epicworld.viewmodel

import com.ruben.epicworld.CoroutinesTestRule
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.domain.interactor.GetGameDetailsUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.details.GameDetailsViewModel
import com.ruben.epicworld.presentation.details.ui.GameDetailsState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `vm should invoke use case to get game details`() = coroutinesTestRule.testCoroutineScope.runBlockingTest {
        val gameDetailsViewModel = GameDetailsViewModel(
            useCase,
            coroutinesTestRule.testDispatcher
        ).test(initialState = initialState)
        every { runBlocking { mockRepository.getGameDetails(2) } } answers {
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
            useCase,
            coroutinesTestRule.testDispatcher
        ).test(initialState = initialState)
        gameDetailsViewModel.testIntent {
            handleGameIdError()
        }
        gameDetailsViewModel.sideEffectObserver.awaitCount(1)
        Assert.assertTrue(gameDetailsViewModel.sideEffectObserver.values.isNotEmpty())
    }

    @Test
    fun `vm should post side effect when there is error with game id`() = coroutinesTestRule.testCoroutineScope.runBlockingTest {
        val gameDetailsViewModel = GameDetailsViewModel(
            useCase,
            coroutinesTestRule.testDispatcher
        ).test(initialState = initialState)
        gameDetailsViewModel.testIntent {
            handleGameIdError()
        }
        gameDetailsViewModel.sideEffectObserver.awaitCount(1)
        Assert.assertTrue(gameDetailsViewModel.sideEffectObserver.values.isNotEmpty())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}