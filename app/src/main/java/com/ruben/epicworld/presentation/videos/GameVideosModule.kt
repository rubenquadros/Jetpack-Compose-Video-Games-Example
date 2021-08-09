package com.ruben.epicworld.presentation.videos

import com.ruben.epicworld.domain.interactor.GetGameVideosUseCase
import com.ruben.epicworld.domain.repository.GamesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by Ruben Quadros on 09/08/21
 **/
@Module
@InstallIn(ViewModelComponent::class)
object GameVideosModule {

    @Provides
    fun providesGetGameVideosUseCase(gamesRepository: GamesRepository) = GetGameVideosUseCase(gamesRepository)
}