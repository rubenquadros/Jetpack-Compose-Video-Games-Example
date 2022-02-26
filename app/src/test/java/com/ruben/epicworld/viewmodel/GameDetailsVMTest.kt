package com.ruben.epicworld.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.domain.interactor.GetGameDetailsUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.details.GameDetailsSideEffect
import com.ruben.epicworld.presentation.details.GameDetailsState
import com.ruben.epicworld.presentation.details.GameDetailsViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.test

/**
 * Created by Ruben Quadros on 07/08/21
 **/
@ExperimentalCoroutinesApi
class GameDetailsVMTest {

    private val mockRepository = mockk<GamesRepository>()
    private val useCase = GetGameDetailsUseCase(mockRepository)
    private val initialState = GameDetailsState(ScreenState.Loading, null, null)

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `vm should invoke use case to get game details`() = runTest(UnconfinedTestDispatcher()) {
        val savedStateHandle = SavedStateHandle().apply {
            set("gameId", 2)
        }
        val gameDetailsViewModel = GameDetailsViewModel(
            savedStateHandle,
            useCase,
            UnconfinedTestDispatcher()
        ).test(initialState = initialState)

        val mockResponse = GameDetailsEntity()
        coEvery { mockRepository.getGameDetails(2) } answers {
            Record(mockResponse, null)
        }
        val success = gameDetailsViewModel.testIntent {
            getGameDetails(2)
        }

        success.assert(initialState) {
            states(
                { copy(screenState = ScreenState.Success, error = null, gameDetails = mockResponse) }
            )
        }

        coVerify { mockRepository.getGameDetails(2) }
        confirmVerified(mockRepository)
    }

    @Test
    fun `vm should post side effect when there is error in getting game details`() = runTest(UnconfinedTestDispatcher()) {
        val savedStateHandle = SavedStateHandle().apply {
            set("gameId", 2)
        }
        val gameDetailsViewModel = GameDetailsViewModel(
            savedStateHandle,
            useCase,
            UnconfinedTestDispatcher()
        ).test(initialState = initialState)

        coEvery { mockRepository.getGameDetails(2) } answers {
            Record(null, ErrorRecord.GenericError)
        }
        val error = gameDetailsViewModel.testIntent {
            getGameDetails(2)
        }

        error.assert(initialState) {
            states(
                { copy(screenState = ScreenState.Error, gameDetails = null, error = ErrorRecord.GenericError) }
            )
            postedSideEffects(
                GameDetailsSideEffect.ShowGameDetailsErrorToast
            )
        }

        coVerify { mockRepository.getGameDetails(2) }
        confirmVerified(mockRepository)
    }

    @Test
    fun `vm should post side effect when there is error with game id`() = runTest(UnconfinedTestDispatcher()) {
        val savedStateHandle = SavedStateHandle()
        val gameDetailsViewModel = GameDetailsViewModel(
            savedStateHandle,
            useCase,
            UnconfinedTestDispatcher()
        ).test(initialState = initialState)
        val error = gameDetailsViewModel.runOnCreate()
        error.assert(initialState) {
            postedSideEffects(
                GameDetailsSideEffect.ShowGameIdErrorToast
            )
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

}