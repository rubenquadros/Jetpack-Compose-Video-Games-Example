package com.ruben.epicworld.injection

import com.ruben.epicworld.data.DataSource
import com.ruben.epicworld.data.repository.GamesRepositoryImpl
import com.ruben.epicworld.domain.repository.GamesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Ruben Quadros on 01/08/21
 **/
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providesGamesRepository(dataSource: DataSource): GamesRepository =
        GamesRepositoryImpl(dataSource = dataSource)
}