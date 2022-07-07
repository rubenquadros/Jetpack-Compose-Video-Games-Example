package com.ruben.epicworld.domain

import app.cash.turbine.test
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.games.GamesEntity
import com.ruben.epicworld.domain.interactor.GameSearchUseCase
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
 * Created by Ruben Quadros on 18/09/21
 **/
@ExperimentalCoroutinesApi
class GameSearchUseCaseTest {

    private val mockGamesRepository = mockk<GamesRepository>()
    private val gameSearchUseCase = GameSearchUseCase(mockGamesRepository)

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `should get search results entity from repository`() = runTest(UnconfinedTestDispatcher()) {
        coEvery { mockGamesRepository.searchGames("gta") } answers {
            Record(GamesEntity(), null)
        }
        val resultFlow = gameSearchUseCase.invoke(
            GameSearchUseCase.RequestValue("gta")
        )
        resultFlow.test {
            coVerify { mockGamesRepository.searchGames("gta") }
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