package com.ruben.epicworld.presentation.home

import com.ruben.epicworld.domain.interactor.GamesSource
import com.ruben.epicworld.domain.repository.GamesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by Ruben Quadros on 01/08/21
 **/
@Module
@InstallIn(ViewModelComponent::class)
internal object HomeModule {

    @Provides
    fun providesGamesSource(gamesRepository: GamesRepository) = GamesSource(gamesRepository = gamesRepository)
}