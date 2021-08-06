package com.ruben.epicworld.remote

import com.ruben.epicworld.remote.rest.RestApi

/**
 * Created by Ruben Quadros on 01/08/21
 **/
interface NetworkManager {
    fun restApi(): RestApi
}