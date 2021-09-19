package com.ruben.epicworld.domain

import com.ruben.epicworld.CoroutinesTestRule
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.games.GamesEntity
import com.ruben.epicworld.domain.interactor.GameSearchUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Ruben Quadros on 18/09/21
 **/
@ExperimentalCoroutinesApi
class GameSearchUseCaseTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val mockGamesRepository = mockk<GamesRepository>()
    private val gameSearchUseCase = GameSearchUseCase(mockGamesRepository)

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `should get search results entity from repository`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        every { runBlocking { mockGamesRepository.searchGames("gta") } } answers {
            Record(GamesEntity(), null)
        }
        gameSearchUseCase.invoke(
            coroutinesTestRule.testCoroutineScope,
            coroutinesTestRule.testDispatcher,
            GameSearchUseCase.RequestValue("gta")
        ) {
            verify { runBlocking { mockGamesRepository.searchGames("gta") } }
            confirmVerified(mockGamesRepository)
            Assert.assertTrue(it?.data != null)
            Assert.assertTrue(it?.error == null)
        }
    }

}