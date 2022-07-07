package com.ruben.epicworld.domain

import app.cash.turbine.test
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamevideos.GameVideosEntity
import com.ruben.epicworld.domain.interactor.GetGameVideosUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Ruben Quadros on 11/08/21
 **/
@ExperimentalCoroutinesApi
class GetGameVideosUseCaseTest {

    private val mockGamesRepository = mockk<GamesRepository>()
    private val getGameVideosUseCase = GetGameVideosUseCase(mockGamesRepository)

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `should get game videos from repository`() = runTest(UnconfinedTestDispatcher()) {
        coEvery { mockGamesRepository.getGameVideos(1) } answers {
            Record(GameVideosEntity(), null)
        }
        val resultFlow = getGameVideosUseCase.invoke(
            GetGameVideosUseCase.RequestValue(1)
        )

        resultFlow.test {
            coVerify { mockGamesRepository.getGameVideos(1) }
            confirmVerified(mockGamesRepository)

            val result = awaitItem()
            Assert.assertTrue(result.data != null)
            Assert.assertTrue(result.error == null)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }
}