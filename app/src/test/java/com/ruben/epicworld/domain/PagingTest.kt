package com.ruben.epicworld.domain

import androidx.paging.PagingSource
import com.ruben.epicworld.domain.entity.base.ErrorRecord
import com.ruben.epicworld.domain.entity.base.Record
import com.ruben.epicworld.domain.interactor.GamesSource
import com.ruben.epicworld.domain.repository.GamesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class PagingTest {

    private val mockRepository = mockk<GamesRepository>()

    @Before
    fun init() {
        MockKAnnotations.init(this, true)
    }

    @Test
    fun `should get same amount of result as specified in paging load`() = runBlocking {
        coEvery { mockRepository.getAllGames(any()) } returns Record(FakeData.getFakeGames(), null)
        val pagingSource = GamesSource(mockRepository)
        Assert.assertEquals(
            PagingSource.LoadResult.Page(
                data = FakeData.getFakeGames().gameEntities,
                prevKey = null,
                nextKey = 2
            ),
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `should get error if server gives error response`() = runBlocking {
        coEvery { mockRepository.getAllGames(any()) } returns Record(null, ErrorRecord.GenericError)
        val pagingSource = GamesSource(mockRepository)
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )
        Assert.assertTrue(result is PagingSource.LoadResult.Error)
    }
}