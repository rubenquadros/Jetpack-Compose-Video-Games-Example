package com.ruben.epicworld.data

import com.ruben.epicworld.remote.NetworkManager
import com.ruben.epicworld.remote.NetworkManagerImpl
import com.ruben.epicworld.remote.rest.RestApi
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class DataSourceImpl @Inject constructor(private val restApi: RestApi): DataSource {
    override fun api(): NetworkManager = NetworkManagerImpl(restApi = restApi)
}