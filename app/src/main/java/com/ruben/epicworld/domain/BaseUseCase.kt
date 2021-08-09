package com.ruben.epicworld.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Created by Ruben Quadros on 01/08/21
 **/
abstract class BaseUseCase<REQUEST, RESPONSE> {

    operator fun invoke(
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        request: REQUEST,
        onResponse: (RESPONSE?) -> Unit
    ) {
        val job = scope.async(dispatcher) {
            run(request = request)
        }
        scope.launch(dispatcher) {
            if (isActive) {
                try {
                    onResponse(job.await())
                } catch (e: Exception) {
                    onResponse(null)
                }
            }
        }
    }

    abstract suspend fun run(request: REQUEST): RESPONSE
}