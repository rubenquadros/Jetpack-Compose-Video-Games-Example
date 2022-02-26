package com.ruben.epicworld.domain

import app.cash.turbine.test
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.domain.interactor.GetGameDetailsUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Ruben Quadros on 07/08/21
 **/
@ExperimentalCoroutinesApi
class GetGameDetailsUseCaseTest {

    private val mockGamesRepository = mockk<GamesRepository>()
    private val getGameDetailsUseCase = GetGameDetailsUseCase(mockGamesRepository)

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `should get game details entity from repository`() = runTest(UnconfinedTestDispatcher()) {
        coEvery { mockGamesRepository.getGameDetails(2) } answers {
            Record(GameDetailsEntity(), null)
        }
        val resultFlow = getGameDetailsUseCase.invoke(
            UnconfinedTestDispatcher(),
            GetGameDetailsUseCase.RequestValue(2)
        )
        resultFlow.test {
            coVerify { mockGamesRepository.getGameDetails(2) }
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