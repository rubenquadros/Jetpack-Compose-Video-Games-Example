package com.ruben.epicworld.data

import com.ruben.epicworld.remote.NetworkManager

/**
 * Created by Ruben Quadros on 01/08/21
 **/
interface DataSource {
    fun api(): NetworkManager
}