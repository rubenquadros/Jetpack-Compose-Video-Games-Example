package com.ruben.epicworld.repository.fakeimplementation.fail

import com.ruben.epicworld.data.DataSource
import com.ruben.epicworld.remote.NetworkManager

/**
 * Created by Ruben Quadros on 01/08/21
 **/
class FakeFailDataSource: DataSource {
    override fun api(): NetworkManager = FakeFailNetworkManager()
}