package com.ruben.epicworld

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Created by Ruben Quadros on 07/08/21
 **/
@ExperimentalCoroutinesApi
class CoroutinesTestRule: TestWatcher() {

    val testDispatcher = TestCoroutineDispatcher()
    val testCoroutineScope = TestCoroutineScope(testDispatcher)

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testCoroutineScope.cleanupTestCoroutines()
    }
}