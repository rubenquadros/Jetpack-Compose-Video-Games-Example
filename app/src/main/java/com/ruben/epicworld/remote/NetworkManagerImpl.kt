package com.ruben.epicworld.remote

import com.ruben.epicworld.remote.rest.RestApi
import javax.inject.Inject

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class NetworkManagerImpl @Inject constructor(private val restApi: RestApi): NetworkManager {

    override fun restApi(): RestApi = restApi
}