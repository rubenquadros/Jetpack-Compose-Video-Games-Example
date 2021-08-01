package com.ruben.epicworld.repository

import com.ruben.epicworld.data.repository.GamesRepositoryImpl
import com.ruben.epicworld.domain.repository.GamesRepository
import com.ruben.epicworld.repository.fakeimplementation.fail.FakeFailDataSource
import com.ruben.epicworld.repository.fakeimplementation.success.FakeSuccessDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class GameRepositoryTest {

    @Test
    fun `should get games entity when server gives success response`() = runBlocking {
        val repository: GamesRepository = GamesRepositoryImpl(FakeSuccessDataSource())
        val result = repository.getAllGames(1)
        Assert.assertTrue(result.error == null)
        Assert.assertTrue(result.data?.gameEntities != null)
    }

    @Test
    fun `should get error entity when server gives fail response`() = runBlocking {
        val repository: GamesRepository = GamesRepositoryImpl(FakeFailDataSource())
        val result = repository.getAllGames(1)
        Assert.assertTrue(result.data == null)
        Assert.assertTrue(result.error != null)
    }
}