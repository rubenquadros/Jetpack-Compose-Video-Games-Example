package com.ruben.epicworld.presentation.details

import com.ruben.epicworld.domain.interactor.GetGameDetailsUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by Ruben Quadros on 06/08/21
 **/
@Module
@InstallIn(ViewModelComponent::class)
object GameDetailsModule {

    @Provides
    fun providesGetGameDetailsUseCase(gamesRepository: GamesRepository) = GetGameDetailsUseCase(gamesRepository)
}