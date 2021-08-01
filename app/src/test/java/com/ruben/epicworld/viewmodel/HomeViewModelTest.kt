package com.ruben.epicworld.viewmodel

import androidx.paging.PagingSource
import com.ruben.epicworld.domain.interactor.GamesSource
import com.ruben.epicworld.presentation.home.HomeViewModel
import com.ruben.epicworld.presentation.home.ui.HomeIntent
import com.ruben.epicworld.presentation.home.ui.HomeState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Ruben Quadros on 02/08/21
 **/
class HomeViewModelTest {

    private val mockGamesSource = mockk<GamesSource>()
    private val homeViewModel = HomeViewModel(mockGamesSource)

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `dispatching all games intent the vm should get all games`() {
        every { runBlocking { mockGamesSource.load(any()) } } returns PagingSource.LoadResult.Page(
            data = arrayListOf(),
            nextKey = 2,
            prevKey = null
        )
        homeViewModel.dispatchIntent(HomeIntent.AllGames)
        Assert.assertTrue(homeViewModel.state.value != HomeState.LoadingState)
        Assert.assertTrue(homeViewModel.state.value != HomeState.InitialState)
    }
}