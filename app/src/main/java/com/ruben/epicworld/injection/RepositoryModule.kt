package com.ruben.epicworld.injection

import com.ruben.epicworld.data.repository.GamesRepositoryImpl
import com.ruben.epicworld.domain.repository.GamesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Ruben Quadros on 01/08/21
 **/
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds Singleton]
    fun providesGamesRepository(gamesRepositoryImpl: GamesRepositoryImpl): GamesRepository
}