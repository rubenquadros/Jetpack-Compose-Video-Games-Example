package com.ruben.epicworld.repository.fakeimplementation.fail

import com.ruben.epicworld.remote.NetworkManager
import com.ruben.epicworld.remote.rest.RestApi

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class FakeFailNetworkManager: NetworkManager {
    override fun restApi(): RestApi = FakeFailApi()
}