package com.ruben.epicworld.repository.fakeimplementation.success

import com.ruben.epicworld.data.DataSource
import com.ruben.epicworld.remote.NetworkManager

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class FakeSuccessDataSource: DataSource {
    override fun api(): NetworkManager = FakeSuccessNetworkManager()
}