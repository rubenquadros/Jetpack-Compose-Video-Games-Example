package com.ruben.epicworld.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.domain.interactor.GetGameVideosUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import com.ruben.epicworld.presentation.base.ScreenState
import com.ruben.epicworld.presentation.videos.GameVideosSideEffect
import com.ruben.epicworld.presentation.videos.GameVideosState
import com.ruben.epicworld.presentation.videos.GameVideosViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.test

/**
 * Created by Ruben Quadros on 11/08/21
 **/
@ExperimentalCoroutinesApi
class GameVideosVMTest {

    private val mockRepository = mockk<GamesRepository>()
    private val useCase = GetGameVideosUseCase(mockRepository)
    private val initialState = GameVideosState(ScreenState.Loading, null, null)

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `vm should invoke use case to get game videos`() = runTest(UnconfinedTestDispatcher()) {
        val savedStateHandle = SavedStateHandle().apply {
            set("gameId", 1)
        }
        val mockResponse = GameVideosEntity(count = 5, results = emptyList())
        val gameVideosViewModel = GameVideosViewModel(
            savedStateHandle,
            useCase
        ).test(initialState = initialState)
        coEvery { mockRepository.getGameVideos(1) } answers {
            Record(data = mockResponse, error = null)
        }

        val success = gameVideosViewModel.testIntent {
            getGameVideos(1)
        }
        success.assert(initialState) {
            states(
                { copy(screenState = ScreenState.Success, error = null, gameVideos = mockResponse) }
            )
        }

        coVerify { mockRepository.getGameVideos(1) }
        confirmVerified(mockRepository)
    }

    @Test
    fun `vm should post side effect when there is error in getting game videos`() = runTest(UnconfinedTestDispatcher()) {
        val savedStateHandle = SavedStateHandle().apply {
            set("gameId", 1)
        }
        val gameVideosViewModel = GameVideosViewModel(
            savedStateHandle,
            useCase
        ).test(initialState = initialState)
        coEvery { mockRepository.getGameVideos(1) } answers {
            Record(null, ErrorRecord.ServerError)
        }

        val error = gameVideosViewModel.testIntent {
            getGameVideos(1)
        }
        error.assert(initialState) {
            states(
                { copy(screenState = ScreenState.Error, gameVideos = null, error = ErrorRecord.ServerError) }
            )
            postedSideEffects(
                GameVideosSideEffect.GameVideosError
            )
        }

        coVerify { mockRepository.getGameVideos(1) }
        confirmVerified(mockRepository)
    }

    @Test
    fun `vm should post side effect when there is error with game id`() = runTest(UnconfinedTestDispatcher()) {
        val savedStateHandle = SavedStateHandle()
        val gameVideosViewModel = GameVideosViewModel(
            savedStateHandle,
            useCase
        ).test(initialState = initialState)

        val error = gameVideosViewModel.runOnCreate()
        error.assert(initialState) {
            postedSideEffects(
                GameVideosSideEffect.ShowGameIdErrorToast
            )
        }
    }

    @Test
    fun `vm should post side effect when there are no videos for the game`() = runTest(UnconfinedTestDispatcher()) {
        val savedStateHandle = SavedStateHandle().apply {
            set("gameId", 1)
        }
        val gameVideosViewModel = GameVideosViewModel(
            savedStateHandle,
            useCase
        ).test(initialState = initialState)
        coEvery { mockRepository.getGameVideos(1) } answers {
            Record(GameVideosEntity(), null)
        }

        val success = gameVideosViewModel.testIntent {
            getGameVideos(1)
        }
        success.assert(initialState) {
            postedSideEffects(
                GameVideosSideEffect.ShowNoGameVideosToast
            )
        }

        coVerify { mockRepository.getGameVideos(1) }
        confirmVerified(mockRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }
}