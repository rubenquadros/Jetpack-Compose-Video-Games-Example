package com.ruben.epicworld.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Ruben Quadros on 01/08/21
 **/
abstract class BaseUseCase<REQUEST, RESPONSE> {

    suspend operator fun invoke(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        request: REQUEST
    ): Flow<RESPONSE> = flow {
        emit(run(request = request))
    }.flowOn(dispatcher)

    abstract suspend fun run(request: REQUEST): RESPONSE
}