package com.ruben.epicworld.injection

import android.content.Context
import com.ruben.epicworld.data.DataSource
import com.ruben.epicworld.data.DataSourceImpl
import com.ruben.epicworld.remote.rest.RestApi
import com.ruben.epicworld.remote.rest.RestApiImpl
import com.ruben.epicworld.remote.retrofit.RetrofitApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

/**
 * Created by Ruben Quadros on 01/08/21
 **/
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesContext(@ApplicationContext context: Context) = context

    @Provides
    fun providesRestApi(retrofitApi: RetrofitApi): RestApi = RestApiImpl(retrofitApi = retrofitApi)

    @Provides
    fun providesDataSource(restApi: RestApi): DataSource = DataSourceImpl(restApi = restApi)

    @Provides
    fun providesDispatcher() = Dispatchers.IO
}