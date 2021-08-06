package com.ruben.epicworld.domain

import com.ruben.epicworld.CoroutinesTestRule
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.entity.gamedetails.GameDetailsEntity
import com.ruben.epicworld.domain.interactor.GetGameDetailsUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Ruben Quadros on 07/08/21
 **/
@ExperimentalCoroutinesApi
class GetGameDetailsUseCaseTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val mockGamesRepository = mockk<GamesRepository>()
    private val getGameDetailsUseCase = GetGameDetailsUseCase(mockGamesRepository)

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `should get game details entity from repository`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        every { runBlocking { mockGamesRepository.getGameDetails(2) } } answers {
            Record(GameDetailsEntity(), null)
        }
        getGameDetailsUseCase.invoke(
            coroutinesTestRule.testCoroutineScope,
            coroutinesTestRule.testDispatcher,
            GetGameDetailsUseCase.RequestValue(2)
        ) {
            verify { runBlocking { mockGamesRepository.getGameDetails(2) } }
            confirmVerified(mockGamesRepository)
        }
    }
}